package org.example.repositories;

import org.example.config.ConfigDB;
import org.example.models.Usuario;
import org.example.models.UsuarioMembresia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioMembresiaRepository {

    private UsuarioRepository usuarioRepository = new UsuarioRepository();


    public UsuarioMembresia save(UsuarioMembresia um) throws SQLException {
        String sql = "INSERT INTO USUARIO_MEMBRESIA (id_usuario, id_membresia_tipo, fecha_inicio, fecha_expiracion, activa) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, um.getId_usuario());
            pstmt.setInt(2, um.getId_membresia_tipo());
            pstmt.setTimestamp(3, Timestamp.valueOf(um.getFecha_inicio()));
            if (um.getFecha_expiracion() != null) {
                pstmt.setTimestamp(4, Timestamp.valueOf(um.getFecha_expiracion()));
            } else {
                pstmt.setNull(4, Types.TIMESTAMP);
            }
            pstmt.setBoolean(5, um.isActiva());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    um.setId_usuario_membresia(generatedKeys.getInt(1));
                }
            }
        }
        return findById(um.getId_usuario_membresia()).orElse(um);
    }

    public void update(UsuarioMembresia um) throws SQLException {
        String sql = "UPDATE USUARIO_MEMBRESIA SET id_usuario = ?, id_membresia_tipo = ?, fecha_inicio = ?, fecha_expiracion = ?, activa = ? WHERE id_usuario_membresia = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, um.getId_usuario());
            pstmt.setInt(2, um.getId_membresia_tipo());
            pstmt.setTimestamp(3, Timestamp.valueOf(um.getFecha_inicio()));
            pstmt.setTimestamp(4, Timestamp.valueOf(um.getFecha_expiracion()));
            pstmt.setBoolean(5, um.isActiva());
            pstmt.setInt(6, um.getId_usuario_membresia());
            pstmt.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM USUARIO_MEMBRESIA WHERE id_usuario_membresia = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Optional<UsuarioMembresia> findById(int id) throws SQLException {
        String sql = "SELECT * FROM USUARIO_MEMBRESIA WHERE id_usuario_membresia = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUsuarioMembresia(rs));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<UsuarioMembresia> findActiveByUserId(int id_usuario) throws SQLException {
        String sql = "SELECT * FROM USUARIO_MEMBRESIA WHERE id_usuario = ? AND activa = TRUE AND fecha_expiracion > NOW() ORDER BY fecha_expiracion DESC LIMIT 1";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id_usuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUsuarioMembresia(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<UsuarioMembresia> findAll() throws SQLException {
        List<UsuarioMembresia> membresias = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO_MEMBRESIA";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                membresias.add(mapRowToUsuarioMembresia(rs));
            }
        }
        return membresias;
    }

    private UsuarioMembresia mapRowToUsuarioMembresia(ResultSet rs) throws SQLException {
        return new UsuarioMembresia(
                rs.getInt("id_usuario_membresia"),
                rs.getInt("id_usuario"),
                rs.getInt("id_membresia_tipo"),
                rs.getTimestamp("fecha_inicio").toLocalDateTime(),
                rs.getTimestamp("fecha_expiracion").toLocalDateTime(),
                rs.getBoolean("activa")
        );
    }
}