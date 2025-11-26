package org.example.repositories;

import org.example.config.ConfigDB;
import org.example.models.QuejaVenta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuejaVentaRepository {

    public QuejaVenta save(QuejaVenta queja) throws SQLException {
        String sql = "INSERT INTO QUEJA_VENTA (id_venta, id_emisor, descripcion_queja, tipo_problema, imagen) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, queja.getId_venta());
            pstmt.setInt(2, queja.getId_emisor());
            pstmt.setString(3, queja.getDescripcion_queja());
            pstmt.setString(4, queja.getTipo_problema());
            pstmt.setBytes(5, queja.getImagen());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    queja.setId(generatedKeys.getInt(1));
                }
            }
        }
        return findById(queja.getId()).orElse(queja);
    }

    public void update(QuejaVenta queja) throws SQLException {
        String sql = "UPDATE QUEJA_VENTA SET id_venta = ?, id_emisor = ?, descripcion_queja = ?, estado_queja = ?, tipo_problema = ?, imagen = ? WHERE id = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, queja.getId_venta());
            pstmt.setInt(2, queja.getId_emisor());
            pstmt.setString(3, queja.getDescripcion_queja());
            if (queja.getEstado_queja() != null) {
                pstmt.setString(4, queja.getEstado_queja());
            } else {
                pstmt.setNull(4, Types.VARCHAR);
            }
            pstmt.setString(5, queja.getTipo_problema());
            pstmt.setBytes(6, queja.getImagen());
            pstmt.setInt(7, queja.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM QUEJA_VENTA WHERE id = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Optional<QuejaVenta> findById(int id) throws SQLException {
        String sql = "SELECT * FROM QUEJA_VENTA WHERE id = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToQuejaVenta(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<QuejaVenta> findAll() throws SQLException {
        List<QuejaVenta> quejas = new ArrayList<>();
        String sql = "SELECT * FROM QUEJA_VENTA ORDER BY fecha_emision DESC";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                quejas.add(mapRowToQuejaVenta(rs));
            }
        }
        return quejas;
    }

    private QuejaVenta mapRowToQuejaVenta(ResultSet rs) throws SQLException {
        Timestamp fechaEmisionTimestamp = rs.getTimestamp("fecha_emision");
        return new QuejaVenta(
                rs.getInt("id"),
                rs.getInt("id_venta"),
                rs.getInt("id_emisor"),
                rs.getString("descripcion_queja"),
                fechaEmisionTimestamp != null ? fechaEmisionTimestamp.toLocalDateTime() : null,
                rs.getString("estado_queja"),
                rs.getString("tipo_problema"),
                rs.getBytes("imagen")
        );
    }
}