package org.example.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.config.ConfigDB;
import org.example.models.Notificacion;

public class NotificacionRepository {

    public Notificacion save(Notificacion notificacion) throws SQLException {
        String sql = "INSERT INTO NOTIFICACION (id_usuario, mensaje, leida, tipo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, notificacion.getId_usuario());
            pstmt.setString(2, notificacion.getMensaje());
            pstmt.setBoolean(3, notificacion.isLeida());
            pstmt.setString(4, notificacion.getTipo());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    notificacion.setId(generatedKeys.getInt(1));
                }
            }
        }
        return findById(notificacion.getId()).orElse(notificacion);
    }

    public void update(Notificacion notificacion) throws SQLException {
        String sql = "UPDATE NOTIFICACION SET id_usuario = ?, mensaje = ?, leida = ?, tipo = ? WHERE id = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, notificacion.getId_usuario());
            pstmt.setString(2, notificacion.getMensaje());
            pstmt.setBoolean(3, notificacion.isLeida());
            pstmt.setString(4, notificacion.getTipo());
            pstmt.setInt(5, notificacion.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM NOTIFICACION WHERE id = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Optional<Notificacion> findById(int id) throws SQLException {
        String sql = "SELECT * FROM NOTIFICACION WHERE id = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToNotificacion(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Notificacion> findAll() throws SQLException {
        List<Notificacion> notificaciones = new ArrayList<>();
        String sql = "SELECT * FROM NOTIFICACION ORDER BY fecha_envio DESC";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notificaciones.add(mapRowToNotificacion(rs));
            }
        }
        return notificaciones;
    }

    public List<Notificacion> findByUserId(int idUsuario) throws SQLException {
        List<Notificacion> notificaciones = new ArrayList<>();
        String sql = "SELECT * FROM NOTIFICACION WHERE id_usuario = ? ORDER BY fecha_envio DESC";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notificaciones.add(mapRowToNotificacion(rs));
                }
            }
        }
        return notificaciones;
    }

    private Notificacion mapRowToNotificacion(ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("fecha_envio");
        return new Notificacion(
                rs.getInt("id"),
                rs.getInt("id_usuario"),
                rs.getString("mensaje"),
                timestamp != null ? timestamp.toLocalDateTime() : null,
                rs.getBoolean("leida"),
                rs.getString("tipo")
        );
    }
}