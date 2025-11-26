package org.example.repositories;

import org.example.config.ConfigDB;
import org.example.models.MembresiaTipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MembresiaTipoRepository {

    public MembresiaTipo save(MembresiaTipo membresiaTipo) throws SQLException {
        String sql = "INSERT INTO MEMBRESIA_TIPO (precio, descripcion) VALUES (?, ?)";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setFloat(1, membresiaTipo.getPrecio());
            pstmt.setString(2, membresiaTipo.getDescripcion());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    membresiaTipo.setId_membresia_tipo(generatedKeys.getInt(1));
                }
            }
        }
        return membresiaTipo;
    }

    public void update(MembresiaTipo membresiaTipo) throws SQLException {
        String sql = "UPDATE MEMBRESIA_TIPO SET precio = ?, descripcion = ? WHERE id_membresia_tipo = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setFloat(1, membresiaTipo.getPrecio());
            pstmt.setString(2, membresiaTipo.getDescripcion());
            pstmt.setInt(3, membresiaTipo.getId_membresia_tipo());
            pstmt.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM MEMBRESIA_TIPO WHERE id_membresia_tipo = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Optional<MembresiaTipo> findById(int id) throws SQLException {
        String sql = "SELECT * FROM MEMBRESIA_TIPO WHERE id_membresia_tipo = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToMembresiaTipo(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<MembresiaTipo> findAll() throws SQLException {
        List<MembresiaTipo> membresiaTipos = new ArrayList<>();
        String sql = "SELECT * FROM MEMBRESIA_TIPO";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                membresiaTipos.add(mapRowToMembresiaTipo(rs));
            }
        }
        return membresiaTipos;
    }

    private MembresiaTipo mapRowToMembresiaTipo(ResultSet rs) throws SQLException {
        return new MembresiaTipo(
                rs.getInt("id_membresia_tipo"),
                rs.getFloat("precio"),
                rs.getString("descripcion")
        );
    }
}