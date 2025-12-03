package org.example.repositories;

import org.example.config.ConfigDB;
import org.example.models.HoraEntrega;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HoraEntregaRepository {

    public void save(int idUsuario, int idHorario) throws SQLException {

        String sql = "INSERT INTO VENDEDOR_HORARIOS (vendedor_id, horario_id) VALUES (?, ?)";

        try (Connection conn = ConfigDB.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idHorario);
            stmt.executeUpdate();
        }
    }

    public void delete(int id_horario) throws Exception {
        String sql = "DELETE FROM VENDEDOR_HORARIOS WHERE horario_id = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_horario);
            stmt.executeUpdate();
        }
    }

    public List<HoraEntrega> findAll() throws SQLException {
        List<HoraEntrega> horaEntregas = new ArrayList<>();
        String sql = "SELECT * FROM HORARIO";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                horaEntregas.add(mapRowToHorario(rs));
            }
        }
        return horaEntregas;
    }

    public List<HoraEntrega> findById(int idUsuario) throws SQLException {
        List<HoraEntrega> horarios = new ArrayList<>();

        String sql = """
                    SELECT
                    v.id_usuario,
                    h.hora,
                    h.id
                    FROM VENDEDOR_HORARIOS vh
                    inner join USUARIO v on vh.vendedor_id = v.id_usuario
                    inner JOIN HORARIO h
                    ON vh.horario_id = h.id where v.id_usuario=?;
                    """;

        try (Connection conn = ConfigDB.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                horarios.add(mapRowToHorarioDetalleCompleto(rs));
            }
        }

        return horarios;
    }

    private HoraEntrega mapRowToHorario(ResultSet rs) throws SQLException {
        return new HoraEntrega(
                rs.getInt("id"),
                rs.getInt("hora")
        );
    }

    private HoraEntrega mapRowToHorarioDetalleCompleto(ResultSet rs) throws SQLException {
        return new HoraEntrega(
                rs.getInt("id"),
                rs.getInt("hora"),
                rs.getInt("id_usuario")
        );
    }
}
