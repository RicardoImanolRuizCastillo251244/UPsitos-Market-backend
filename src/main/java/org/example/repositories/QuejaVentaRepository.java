package org.example.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.config.ConfigDB;
import org.example.models.QuejaVenta;
import org.example.models.QuejaVentaDTO;

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

    public List<QuejaVentaDTO> findAllWithDetails() throws SQLException {
        List<QuejaVentaDTO> quejas = new ArrayList<>();
        String sql = "SELECT " +
                "qv.id, " +
                "qv.id_emisor, " +
                "qv.imagen as imagen_queja, " +
                "emisor.nombre_usuario as emisor_nombre, " +
                "emisor.correo_usuario as emisor_correo, " +
                "COALESCE(p.titulo_publicacion, 'Sin título') as titulo_publicacion, " +
                "p.foto_publicacion, " +
                "qv.descripcion_queja, " +
                "qv.fecha_emision, " +
                "qv.estado_queja, " +
                "qv.tipo_problema, " +
                "qv.id_venta, " +
                "v.tipo_pago, " +
                "v.imagen as imagen_comprobante, " +
                "v.id_comprador, " +
                "comprador.nombre_usuario as comprador_nombre, " +
                "comprador.correo_usuario as comprador_correo, " +
                "p.id_vendedor, " +
                "vendedor.nombre_usuario as vendedor_nombre, " +
                "vendedor.correo_usuario as vendedor_correo " +
                "FROM QUEJA_VENTA qv " +
                "INNER JOIN USUARIO emisor ON qv.id_emisor = emisor.id_usuario " +
                "INNER JOIN VENTA v ON qv.id_venta = v.id " +
                "INNER JOIN PUBLICACION p ON v.id_publicacion = p.id_publicacion " +
                "INNER JOIN USUARIO comprador ON v.id_comprador = comprador.id_usuario " +
                "INNER JOIN USUARIO vendedor ON p.id_vendedor = vendedor.id_usuario " +
                "ORDER BY qv.fecha_emision DESC";
        
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                QuejaVentaDTO dto = new QuejaVentaDTO();
                dto.setId_queja(rs.getInt("id"));
                dto.setId_usuario(rs.getInt("id_emisor"));
                dto.setNombre_usuario(rs.getString("emisor_nombre"));
                dto.setCorreo_usuario(rs.getString("emisor_correo"));
                dto.setTitulo_publicacion(rs.getString("titulo_publicacion"));
                dto.setDescripcion(rs.getString("descripcion_queja"));
                
                Timestamp fechaEmision = rs.getTimestamp("fecha_emision");
                if (fechaEmision != null) {
                    dto.setFecha_creacion(fechaEmision.toLocalDateTime());
                }
                
                dto.setEstado(rs.getString("estado_queja"));
                dto.setTipo_problema(rs.getString("tipo_problema"));
                dto.setId_venta(rs.getInt("id_venta"));
                
                // Información de pago
                dto.setTipo_pago(rs.getString("tipo_pago"));
                
                // Imagen del comprobante de pago (de VENTA)
                byte[] imagenComprobante = rs.getBytes("imagen_comprobante");
                if (imagenComprobante != null && imagenComprobante.length > 0) {
                    String base64Comprobante = "data:image/jpeg;base64," + 
                        java.util.Base64.getEncoder().encodeToString(imagenComprobante);
                    dto.setImagen_comprobante_pago(base64Comprobante);
                }
                
                // Imagen de evidencia de la queja (de QUEJA_VENTA)
                byte[] imagenQueja = rs.getBytes("imagen_queja");
                if (imagenQueja != null && imagenQueja.length > 0) {
                    String base64Queja = "data:image/jpeg;base64," + 
                        java.util.Base64.getEncoder().encodeToString(imagenQueja);
                    dto.setImagen_evidencia_queja(base64Queja);
                }
                
                // Información del comprador
                dto.setId_comprador(rs.getInt("id_comprador"));
                dto.setNombre_comprador(rs.getString("comprador_nombre"));
                dto.setCorreo_comprador(rs.getString("comprador_correo"));
                
                // Información del vendedor
                dto.setId_vendedor(rs.getInt("id_vendedor"));
                dto.setNombre_vendedor(rs.getString("vendedor_nombre"));
                dto.setCorreo_vendedor(rs.getString("vendedor_correo"));
                
                // Agregar foto de la publicación si existe (para contexto)
                byte[] fotoPublicacion = rs.getBytes("foto_publicacion");
                if (fotoPublicacion != null) {
                    dto.addImagen(fotoPublicacion);
                }
                
                quejas.add(dto);
            }
        }
        return quejas;
    }
}