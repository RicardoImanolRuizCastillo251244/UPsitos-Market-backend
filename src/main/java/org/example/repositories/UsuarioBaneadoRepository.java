package org.example.repositories;

import org.example.config.ConfigDB;
import org.example.models.UsuarioBaneado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioBaneadoRepository {

    public UsuarioBaneado save(UsuarioBaneado usuarioBaneado) throws SQLException {
        String sql = "INSERT INTO USUARIO_BANEADO (id_usuario, fecha, motivo) VALUES (?, ?, ?)";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuarioBaneado.getId_usuario());
            pstmt.setTimestamp(2, Timestamp.valueOf(usuarioBaneado.getFecha()));
            pstmt.setString(3, usuarioBaneado.getMotivo());
            pstmt.executeUpdate();
        }
        return usuarioBaneado;
    }

    public void deleteById(int id_usuario) throws SQLException {
        String sql = "DELETE FROM USUARIO_BANEADO WHERE id_usuario = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id_usuario);
            pstmt.executeUpdate();
        }
    }

    public Optional<UsuarioBaneado> findById(int id_usuario) throws SQLException {
        String sql = "SELECT * FROM USUARIO_BANEADO WHERE id_usuario = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_usuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUsuarioBaneado(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<UsuarioBaneado> findAll() throws SQLException {
        List<UsuarioBaneado> baneados = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO_BANEADO";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                baneados.add(mapRowToUsuarioBaneado(rs));
            }
        }
        return baneados;
    }

    private UsuarioBaneado mapRowToUsuarioBaneado(ResultSet rs) throws SQLException {
        return new UsuarioBaneado(
                rs.getInt("id_usuario"),
                rs.getTimestamp("fecha").toLocalDateTime(),
                rs.getString("motivo")
        );
    }
}