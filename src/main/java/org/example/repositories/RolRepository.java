package org.example.repositories;

import org.example.config.ConfigDB;
import org.example.models.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RolRepository {

    public Rol save(Rol rol) throws SQLException {
        String sql = "INSERT INTO ROL (descripcion) VALUES (?)";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, rol.getDescripcion());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rol.setId_rol(generatedKeys.getInt(1));
                }
            }
        }
        return rol;
    }

    public void update(Rol rol) throws SQLException {
        String sql = "UPDATE ROL SET descripcion = ? WHERE id_rol = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rol.getDescripcion());
            pstmt.setInt(2, rol.getId_rol());
            pstmt.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM ROL WHERE id_rol = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Optional<Rol> findById(int id) throws SQLException {
        String sql = "SELECT * FROM ROL WHERE id_rol = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToRol(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Rol> findAll() throws SQLException {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT * FROM ROL";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                roles.add(mapRowToRol(rs));
            }
        }
        return roles;
    }

    private Rol mapRowToRol(ResultSet rs) throws SQLException {
        return new Rol(
                rs.getInt("id_rol"),
                rs.getString("descripcion")
        );
    }
}