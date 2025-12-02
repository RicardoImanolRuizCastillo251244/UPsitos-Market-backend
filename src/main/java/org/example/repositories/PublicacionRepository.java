package org.example.repositories;

import org.example.config.ConfigDB;
import org.example.models.Publicacion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PublicacionRepository {

    public Publicacion save(Publicacion pub) throws SQLException {
        String sql = "INSERT INTO PUBLICACION (titulo_publicacion, descripcion_publicacion, foto_publicacion, fecha_expiracion, id_vendedor, precio_producto, id_categoria, existencia_publicacion) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, pub.getTitulo_publicacion());
            pstmt.setString(2, pub.getDescripcion_publicacion());
            pstmt.setBytes(3, pub.getFoto_publicacion());
            if (pub.getFecha_expiracion() != null) {
                pstmt.setTimestamp(4, Timestamp.valueOf(pub.getFecha_expiracion()));
            } else {
                pstmt.setNull(4, Types.TIMESTAMP);
            }
            pstmt.setInt(5, pub.getId_vendedor());
            pstmt.setFloat(6, pub.getPrecio_producto());
            if (pub.getId_categoria() != 0) {
                pstmt.setInt(7, pub.getId_categoria());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }
            pstmt.setInt(8, pub.getExistencia_publicacion());
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
        String sql = "UPDATE PUBLICACION SET titulo_publicacion = ?, descripcion_publicacion = ?, foto_publicacion = ?, fecha_expiracion = ?, estado_publicacion = ?, precio_producto = ?, id_categoria = ?, existencia_publicacion = ? WHERE id_publicacion = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pub.getTitulo_publicacion());
            pstmt.setString(2, pub.getDescripcion_publicacion());
            pstmt.setBytes(3, pub.getFoto_publicacion());
            pstmt.setTimestamp(4, Timestamp.valueOf(pub.getFecha_expiracion()));
            pstmt.setString(5, pub.getEstado_publicacion());
            pstmt.setFloat(6, pub.getPrecio_producto());
            if (pub.getId_categoria() != 0) {
                pstmt.setInt(7, pub.getId_categoria());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }
            pstmt.setInt(8, pub.getId_publicacion());
            pstmt.setInt(9, pub.getExistencia_publicacion());
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
        return new Publicacion(
                rs.getInt("id_publicacion"),
                rs.getString("titulo_publicacion"),
                rs.getString("descripcion_publicacion"),
                rs.getBytes("foto_publicacion"),
                fechaPub != null ? fechaPub.toLocalDateTime() : null,
                fechaExp != null ? fechaExp.toLocalDateTime() : null,
                rs.getString("estado_publicacion"),
                rs.getInt("id_vendedor"),
                rs.getFloat("precio_producto"),
                rs.getInt("id_categoria"),
                rs.getInt("existencia_publicacion")
        );
    }
}