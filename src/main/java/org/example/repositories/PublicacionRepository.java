package org.example.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.config.ConfigDB;
import org.example.models.Publicacion;

public class PublicacionRepository {

    public Publicacion save(Publicacion pub) throws SQLException {
        String sql = "insert into publicacion (titulo_publicacion, descripcion_publicacion, foto_publicacion, estado_publicacion, id_vendedor, precio_producto, id_categoria) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, pub.getTitulo_publicacion());
            pstmt.setString(2, pub.getDescripcion_publicacion());

            if (pub.getFoto_publicacion() != null) {
                pstmt.setBytes(3, pub.getFoto_publicacion());
            } else {
                pstmt.setNull(3, Types.BINARY);
            }

            // estado -> posicion 4
            if (pub.getEstado_publicacion() != null) {
                pstmt.setString(4, pub.getEstado_publicacion());
            } else {
                pstmt.setString(4, "ACTIVA");
            }

            // id_vendedor -> posicion 5
            if (pub.getId_vendedor() != null) {
                pstmt.setInt(5, pub.getId_vendedor());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            // precio_producto -> posicion 6
            if (pub.getPrecio_producto() != null) {
                pstmt.setFloat(6, pub.getPrecio_producto());
            } else {
                pstmt.setNull(6, Types.FLOAT);
            }

            // id_categoria -> posicion 7 (puede ser null)
            if (pub.getId_categoria() != null) {
                pstmt.setInt(7, pub.getId_categoria());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pub.setId_publicacion(generatedKeys.getInt(1));
                }
            }
        }
        return findById(pub.getId_publicacion()).orElse(pub);
    }

    public void update(Publicacion pub) throws SQLException {
        String sql = "UPDATE PUBLICACION SET titulo_publicacion = ?, descripcion_publicacion = ?, foto_publicacion = ?, fecha_expiracion = ?, estado_publicacion = ?, precio_producto = ?, id_categoria = ? WHERE id_publicacion = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pub.getTitulo_publicacion());
            pstmt.setString(2, pub.getDescripcion_publicacion());
            if (pub.getFoto_publicacion() != null) {
                pstmt.setBytes(3, pub.getFoto_publicacion());
            } else {
                pstmt.setNull(3, Types.BINARY);
            }
            if (pub.getFecha_expiracion() != null) {
                pstmt.setTimestamp(4, Timestamp.valueOf(pub.getFecha_expiracion()));
            } else {
                pstmt.setNull(4, Types.TIMESTAMP);
            }
            pstmt.setString(5, pub.getEstado_publicacion());
            if (pub.getPrecio_producto() != null) {
                pstmt.setFloat(6, pub.getPrecio_producto());
            } else {
                pstmt.setNull(6, Types.FLOAT);
            }
            if (pub.getId_categoria() != null) {
                pstmt.setInt(7, pub.getId_categoria());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }
            if (pub.getId_publicacion() != null) {
                pstmt.setInt(8, pub.getId_publicacion());
            } else {
                pstmt.setNull(8, Types.INTEGER);
            }
            pstmt.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        // Borrado lógico en lugar de físico
        String sql = "UPDATE PUBLICACION SET estado_publicacion = 'ELIMINADA' WHERE id_publicacion = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Optional<Publicacion> findById(int id) throws SQLException {
        String sql = "SELECT * FROM PUBLICACION WHERE id_publicacion = ? AND estado_publicacion != 'ELIMINADA'";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToPublicacion(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Publicacion> findAll() throws SQLException {
        List<Publicacion> publicaciones = new ArrayList<>();
        String sql = "SELECT * FROM PUBLICACION WHERE estado_publicacion != 'ELIMINADA'";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                publicaciones.add(mapRowToPublicacion(rs));
            }
        }
        return publicaciones;
    }

    public List<Publicacion> findByCategoria(int idCategoria) throws SQLException {
        List<Publicacion> publicaciones = new ArrayList<>();
        String sql = "SELECT * FROM PUBLICACION WHERE id_categoria = ? AND estado_publicacion != 'ELIMINADA'";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCategoria);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    publicaciones.add(mapRowToPublicacion(rs));
                }
            }
        }
        return publicaciones;
    }

    public List<Publicacion> findByTitulo(String titulo) throws SQLException {
        List<Publicacion> publicaciones = new ArrayList<>();
        String sql = "SELECT * FROM PUBLICACION WHERE titulo_publicacion ILIKE ? AND estado_publicacion != 'ELIMINADA'";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + titulo + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    publicaciones.add(mapRowToPublicacion(rs));
                }
            }
        }
        return publicaciones;
    }

    public List<Publicacion> findPremiumSellerPublications() throws SQLException {
        List<Publicacion> publicaciones = new ArrayList<>();
        String sql = "SELECT p.* " +
                "FROM PUBLICACION p " +
                "JOIN USUARIO u ON p.id_vendedor = u.id_usuario " +
                "JOIN USUARIO_MEMBRESIA um ON u.id_usuario = um.id_usuario " +
                "WHERE u.id_rol = 2 " +
                "  AND um.id_membresia_tipo = 2 " +
                "  AND um.activa = TRUE " +
                "  AND um.fecha_expiracion > NOW() " +
                "  AND p.estado_publicacion = 'ACTIVA' " +
                "ORDER BY p.fecha_publicacion DESC";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                publicaciones.add(mapRowToPublicacion(rs));
            }
        }
        return publicaciones;
    }

    public int countByVendedorInLast24Hours(int id_vendedor) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PUBLICACION WHERE id_vendedor = ? AND fecha_publicacion >= ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id_vendedor);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().minusHours(24)));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    // Obtener publicaciones ordenadas por precio ASC
    public List<Publicacion> findAllOrderByPrecioAsc() throws SQLException {
        String sql = "SELECT * FROM PUBLICACION ORDER BY precio_producto ASC";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Publicacion> publicaciones = new ArrayList<>();
            while (rs.next()) {
                publicaciones.add(mapRowToPublicacion(rs));
            }
            return publicaciones;
        }
    }

    // Obtener publicaciones ordenadas por precio DESC
    public List<Publicacion> findAllOrderByPrecioDesc() throws SQLException {
        String sql = "SELECT * FROM PUBLICACION ORDER BY precio_producto DESC";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Publicacion> publicaciones = new ArrayList<>();
            while (rs.next()) {
                publicaciones.add(mapRowToPublicacion(rs));
            }
            return publicaciones;
        }
    }

    private Publicacion mapRowToPublicacion(ResultSet rs) throws SQLException {
        Timestamp fechaPub = rs.getTimestamp("fecha_publicacion");
        Timestamp fechaExp = rs.getTimestamp("fecha_expiracion");

        Integer id_publicacion = rs.getObject("id_publicacion") != null ? rs.getInt("id_publicacion") : null;
        String titulo = rs.getString("titulo_publicacion");
        String descripcion = rs.getString("descripcion_publicacion");
        byte[] foto = rs.getBytes("foto_publicacion");
        LocalDateTime fecha_publicacion = fechaPub != null ? fechaPub.toLocalDateTime() : null;
        LocalDateTime fecha_expiracion = fechaExp != null ? fechaExp.toLocalDateTime() : null;
        String estado = rs.getString("estado_publicacion");
        Integer id_vendedor = rs.getObject("id_vendedor") != null ? rs.getInt("id_vendedor") : null;
        Object precioObj = rs.getObject("precio_producto");
        Float precio = precioObj != null ? ((Number) precioObj).floatValue() : null;
        Integer id_categoria = (Integer) rs.getObject("id_categoria");

        return new Publicacion(
            id_publicacion,
            titulo,
            descripcion,
            foto,
            fecha_publicacion,
            fecha_expiracion,
            estado,
            id_vendedor,
            precio,
            id_categoria
        );
    }
}