package org.example.repositories;

import org.example.config.ConfigDB;
import org.example.models.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaRepository {

    public Categoria save(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO CATEGORIA (tipo, descripcion) VALUES (?, ?)";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, categoria.getTipo());
            pstmt.setString(2, categoria.getDescripcion());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    categoria.setId_categoria(generatedKeys.getInt(1));
                }
            }
        }
        return categoria;
    }

    public void update(Categoria categoria) throws SQLException {
        String sql = "UPDATE CATEGORIA SET tipo = ?, descripcion = ? WHERE id_categoria = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categoria.getTipo());
            pstmt.setString(2, categoria.getDescripcion());
            pstmt.setInt(3, categoria.getId_categoria());
            pstmt.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM CATEGORIA WHERE id_categoria = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Optional<Categoria> findById(int id) throws SQLException {
        String sql = "SELECT * FROM CATEGORIA WHERE id_categoria = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToCategoria(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Categoria> findAll() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM CATEGORIA";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categorias.add(mapRowToCategoria(rs));
            }
        }
        return categorias;
    }

    private Categoria mapRowToCategoria(ResultSet rs) throws SQLException {
        return new Categoria(
                rs.getInt("id_categoria"),
                rs.getString("tipo"),
                rs.getString("descripcion")
        );
    }
}