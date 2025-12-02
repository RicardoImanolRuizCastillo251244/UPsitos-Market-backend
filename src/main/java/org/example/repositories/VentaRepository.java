package org.example.repositories;

import org.example.config.ConfigDB;
import org.example.models.CompraDTO;
import org.example.models.Venta;

import java.sql.*;
import java.time.LocalDateTime;
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

    public List<CompraDTO> findAllCompras(int id_comprador) throws SQLException {
        List<CompraDTO> compras = new ArrayList<>();
        String sql = """
                SELECT venta.id, venta.cantidad_vendida, venta.fecha_venta, venta.precio_total, venta.hora_entrega,
                venta.fecha_entrega,
                publicacion.id_publicacion, publicacion.titulo_publicacion, publicacion.descripcion_publicacion, publicacion.foto_publicacion,
                publicacion.precio_producto,
                categoria.tipo,
                vendedor.nombre_usuario, vendedor.numero_cuenta, vendedor.titular_cuenta,
                comprador.nombre_usuario
                FROM VENTA venta
                INNER JOIN PUBLICACION publicacion ON venta.id_publicacion = publicacion.id_publicacion
                INNER JOIN USUARIO vendedor ON publicacion.id_vendedor = vendedor.id_usuario
                INNER JOIN USUARIO comprador ON venta.id_comprador = comprador.id_usuario
                INNER JOIN CATEGORIA categoria ON publicacion.id_categoria = categoria.id_categoria
                WHERE venta.id_comprador = ?;""";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
            PreparedStatement  pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,id_comprador);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    compras.add(mapRowToCompra(rs));
                }
            }
            return compras;
        }
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

    private CompraDTO mapRowToCompra(ResultSet rs) throws SQLException {
        Timestamp fechaVentaTS = rs.getTimestamp("fecha_venta");
        LocalDateTime fechaVenta = (fechaVentaTS != null) ? fechaVentaTS.toLocalDateTime() : null;

        Timestamp horaEntregaTS = rs.getTimestamp("hora_entrega");
        LocalDateTime horaEntrega = (horaEntregaTS != null) ? horaEntregaTS.toLocalDateTime() : null;

        Timestamp fechaEntregaTS = rs.getTimestamp("fecha_entrega");
        LocalDateTime fechaEntrega = (fechaEntregaTS != null) ? fechaEntregaTS.toLocalDateTime() : null;
        return new CompraDTO(
                rs.getInt("id"),
                rs.getInt("cantidad_vendida"),
                fechaVenta,
                rs.getDouble("precio_total"),
                horaEntrega,
                fechaEntrega,
                rs.getInt("id_publicacion"),
                rs.getString("titulo_publicacion"),
                rs.getString("descripcion_publicacion"),
                rs.getBytes("foto_publicacion"),
                rs.getDouble("precio_producto"),
                rs.getString("tipo"),
                rs.getString("nombre_usuario"),
                rs.getString("numero_cuenta"),
                rs.getString("titular_cuenta"),
                rs.getString("nombre_usuario")
        );
    }
}