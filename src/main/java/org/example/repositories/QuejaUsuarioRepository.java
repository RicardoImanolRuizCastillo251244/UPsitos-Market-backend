package org.example.repositories;

import org.example.config.ConfigDB;
import org.example.models.QuejaUsuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuejaUsuarioRepository {

    public QuejaUsuario save(QuejaUsuario queja) throws SQLException {
        String sql = "INSERT INTO QUEJA_USUARIO (id_emisor, id_receptor, descripcion_queja, estado_queja, motivo_queja, imagen) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, queja.getId_emisor());
            pstmt.setInt(2, queja.getId_receptor());
            pstmt.setString(3, queja.getDescripcion_queja());
            pstmt.setString(4, queja.getEstado_queja());
            pstmt.setString(5, queja.getMotivo_queja());
            pstmt.setBytes(6, queja.getImagen());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    queja.setId(generatedKeys.getInt(1));
                }
            }
        }
        return findById(queja.getId()).orElse(queja);
    }

    public void update(QuejaUsuario queja) throws SQLException {
        String sql = "UPDATE QUEJA_USUARIO SET id_emisor = ?, id_receptor = ?, descripcion_queja = ?, estado_queja = ?, motivo_queja = ?, imagen = ? WHERE id = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, queja.getId_emisor());
            pstmt.setInt(2, queja.getId_receptor());
            pstmt.setString(3, queja.getDescripcion_queja());
            pstmt.setString(4, queja.getEstado_queja());
            pstmt.setString(5, queja.getMotivo_queja());
            pstmt.setBytes(6, queja.getImagen());
            pstmt.setInt(7, queja.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM QUEJA_USUARIO WHERE id = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Optional<QuejaUsuario> findById(int id) throws SQLException {
        String sql = "SELECT * FROM QUEJA_USUARIO WHERE id = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToQuejaUsuario(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<QuejaUsuario> findAll() throws SQLException {
        List<QuejaUsuario> quejas = new ArrayList<>();
        String sql = "SELECT * FROM QUEJA_USUARIO ORDER BY fecha_emision DESC";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                quejas.add(mapRowToQuejaUsuario(rs));
            }
        }
        return quejas;
    }

    private QuejaUsuario mapRowToQuejaUsuario(ResultSet rs) throws SQLException {
        return new QuejaUsuario(
                rs.getInt("id"),
                rs.getInt("id_emisor"),
                rs.getInt("id_receptor"),
                rs.getString("descripcion_queja"),
                rs.getTimestamp("fecha_emision").toLocalDateTime(),
                rs.getString("estado_queja"),
                rs.getString("motivo_queja"),
                rs.getBytes("imagen")
        );
    }
}