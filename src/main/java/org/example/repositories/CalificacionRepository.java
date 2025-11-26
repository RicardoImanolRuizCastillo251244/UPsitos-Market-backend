package org.example.repositories;

import org.example.config.*;
import org.example.models.Calificacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CalificacionRepository {

    public Calificacion save(Calificacion calificacion) throws SQLException {
        String sql = "INSERT INTO CALIFICACION (id_usuario_califica, id_publicacion, calificacion, experiencia) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, calificacion.getId_usuario_califica());
            pstmt.setInt(2, calificacion.getId_publicacion());
            pstmt.setInt(3, calificacion.getCalificacion());
            pstmt.setString(4, calificacion.getExperiencia());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    calificacion.setId_calificacion(generatedKeys.getInt(1));
                }
            }
        }
        return calificacion;
    }

    public void update(Calificacion calificacion) throws SQLException {
        String sql = "UPDATE CALIFICACION SET id_usuario_califica = ?, id_publicacion = ?, calificacion = ?, experiencia = ? WHERE id_calificacion = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, calificacion.getId_usuario_califica());
            pstmt.setInt(2, calificacion.getId_publicacion());
            pstmt.setInt(3, calificacion.getCalificacion());
            pstmt.setString(4, calificacion.getExperiencia());
            pstmt.setInt(5, calificacion.getId_calificacion());
            pstmt.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM CALIFICACION WHERE id_calificacion = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Optional<Calificacion> findById(int id) throws SQLException {
        String sql = "SELECT * FROM CALIFICACION WHERE id_calificacion = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToCalificacion(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Calificacion> findAll() throws SQLException {
        List<Calificacion> calificaciones = new ArrayList<>();
        String sql = "SELECT * FROM CALIFICACION";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                calificaciones.add(mapRowToCalificacion(rs));
            }
        }
        return calificaciones;
    }

    public boolean existsByUsuarioAndPublicacion(int idUsuario, int idPublicacion) throws SQLException {
        String sql = "SELECT 1 FROM CALIFICACION WHERE id_usuario_califica = ? AND id_publicacion = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idPublicacion);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public Optional<Float> getAverageRatingByPublicationId(int idPublicacion) throws SQLException {
        String sql = "SELECT AVG(calificacion) FROM CALIFICACION WHERE id_publicacion = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPublicacion);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    float average = rs.getFloat(1);
                    return rs.wasNull() ? Optional.empty() : Optional.of(average);
                }
            }
        }
        return Optional.empty();
    }

    private Calificacion mapRowToCalificacion(ResultSet rs) throws SQLException {
        return new Calificacion(
                rs.getInt("id_calificacion"),
                rs.getInt("id_usuario_califica"),
                rs.getInt("id_publicacion"),
                rs.getInt("calificacion"),
                rs.getString("experiencia")
        );
    }
}