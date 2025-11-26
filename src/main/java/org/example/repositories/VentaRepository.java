package org.example.repositories;

import org.example.config.ConfigDB;
import org.example.models.Venta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VentaRepository {

    public Venta save(Venta venta) throws SQLException {
        String sql = "INSERT INTO VENTA (id_publicacion, cantidad_vendida, fecha_venta, precio_total, id_comprador, imagen) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, venta.getId_publicacion());
            pstmt.setInt(2, venta.getCantidad_vendida());
            pstmt.setTimestamp(3, Timestamp.valueOf(venta.getFecha_venta()));
            pstmt.setFloat(4, venta.getPrecio_total());
            pstmt.setInt(5, venta.getId_comprador());
            pstmt.setBytes(6, venta.getImagen());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    venta.setId(generatedKeys.getInt(1));
                }
            }
        }
        return venta;
    }

    public void update(Venta venta) throws SQLException {
        String sql = "UPDATE VENTA SET id_publicacion = ?, cantidad_vendida = ?, fecha_venta = ?, precio_total = ?, id_comprador = ?, imagen = ? WHERE id = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, venta.getId_publicacion());
            pstmt.setInt(2, venta.getCantidad_vendida());
            pstmt.setTimestamp(3, Timestamp.valueOf(venta.getFecha_venta()));
            pstmt.setFloat(4, venta.getPrecio_total());
            pstmt.setInt(5, venta.getId_comprador());
            pstmt.setBytes(6, venta.getImagen());
            pstmt.setInt(7, venta.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM VENTA WHERE id = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Optional<Venta> findById(int id) throws SQLException {
        String sql = "SELECT * FROM VENTA WHERE id = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToVenta(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Venta> findAll() throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM VENTA";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ventas.add(mapRowToVenta(rs));
            }
        }
        return ventas;
    }

    public List<Integer> findPublicacionesMasVendidas() throws SQLException {
        String sql = """
            SELECT v.id_publicacion, SUM(v.cantidad_vendida) AS total_vendido
            FROM VENTA v
            GROUP BY v.id_publicacion
            ORDER BY total_vendido DESC
        """;
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Integer> publicaciones = new ArrayList<>();
            while (rs.next()) {
                publicaciones.add(rs.getInt("id_publicacion"));
            }
            return publicaciones;
        }
    }

    private Venta mapRowToVenta(ResultSet rs) throws SQLException {
        return new Venta(
                rs.getInt("id"),
                rs.getInt("id_publicacion"),
                rs.getInt("cantidad_vendida"),
                rs.getTimestamp("fecha_venta").toLocalDateTime(),
                rs.getFloat("precio_total"),
                rs.getInt("id_comprador"),
                rs.getBytes("imagen")
        );
    }
}