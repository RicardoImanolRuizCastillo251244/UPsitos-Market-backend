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
import org.example.models.Usuario;

public class UsuarioRepository {

    public Usuario save(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO USUARIO (id_rol, nombre_usuario, correo_usuario, contrasena, activo, titular_cuenta, numero_cuenta) VALUES (?, ?, ?, ?, ?, ?,?)";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, usuario.getId_rol());
            pstmt.setString(2, usuario.getNombre_usuario());
            pstmt.setString(3, usuario.getCorreo_usuario());
            pstmt.setString(4, usuario.getContrasena());
            pstmt.setBoolean(5, usuario.isActivo());
            pstmt.setString(6,usuario.getTitular_usuario());
            pstmt.setString(7, usuario.getNumero_cuenta());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId_usuario(generatedKeys.getInt(1));
                }
            }
        }
        return findById(usuario.getId_usuario()).orElse(usuario);
    }

    public void update(Usuario usuario) throws SQLException {
        String sql = "UPDATE USUARIO SET id_rol = ?, nombre_usuario = ?, correo_usuario = ?, contrasena = ?, activo = ?, titular_cuenta = ?, numero_cuenta = ?, actualizado_en = CURRENT_TIMESTAMP WHERE id_usuario = ?";        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuario.getId_rol());
            pstmt.setString(2, usuario.getNombre_usuario());
            pstmt.setString(3, usuario.getCorreo_usuario());
            pstmt.setString(4, usuario.getContrasena());
            pstmt.setBoolean(5, usuario.isActivo());
            pstmt.setInt(6, usuario.getId_usuario());
            pstmt.setString(6, usuario.getTitular_usuario());
            pstmt.setString(7, usuario.getNumero_cuenta());
            pstmt.setInt(8, usuario.getId_usuario());
            pstmt.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM USUARIO WHERE id_usuario = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Optional<Usuario> findById(int id) throws SQLException {
        String sql = "SELECT * FROM USUARIO WHERE id_usuario = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUsuario(rs));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Usuario> findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM USUARIO WHERE correo_usuario = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUsuario(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Usuario> findAll() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usuarios.add(mapRowToUsuario(rs));
            }
        }
        return usuarios;
    }

    public List<Usuario> findUsuariosConMembresiaPremium() throws SQLException {
        String sql = """
            SELECT u.* 
            FROM USUARIO u
            INNER JOIN USUARIO_MEMBRESIA um ON u.id_usuario = um.id_usuario
            INNER JOIN MEMBRESIA_TIPO mt ON um.id_membresia_tipo = mt.id_membresia_tipo
            WHERE mt.precio = 5.00 AND um.activa = TRUE
        """;
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Usuario> usuarios = new ArrayList<>();
            while (rs.next()) {
                usuarios.add(mapRowToUsuario(rs));
            }
            return usuarios;
        }
    }

    public List<Usuario> findUsuariosConMembresiaSemiPremium() throws SQLException {
        String sql = """
            SELECT u.* 
            FROM USUARIO u
            INNER JOIN USUARIO_MEMBRESIA um ON u.id_usuario = um.id_usuario
            INNER JOIN MEMBRESIA_TIPO mt ON um.id_membresia_tipo = mt.id_membresia_tipo
            WHERE mt.precio = 3.00 AND um.activa = TRUE
        """;
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Usuario> usuarios = new ArrayList<>();
            while (rs.next()) {
                usuarios.add(mapRowToUsuario(rs));
            }
            return usuarios;
        }
    }

    public List<Usuario> findAdmins() throws SQLException {
        String sql = "SELECT * FROM USUARIO WHERE id_rol = 1";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Usuario> admins = new ArrayList<>();
            while (rs.next()) {
                admins.add(mapRowToUsuario(rs));
            }
            return admins;
        }
    }

    private Usuario mapRowToUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId_usuario(rs.getInt("id_usuario"));
        u.setId_rol(rs.getInt("id_rol"));
        u.setNombre_usuario(rs.getString("nombre_usuario"));
        u.setCorreo_usuario(rs.getString("correo_usuario"));
        u.setContrasena(rs.getString("contrasena"));
        u.setActivo(rs.getBoolean("activo"));
        Timestamp creadoTs = rs.getTimestamp("creado_en");
        if (creadoTs != null) {
            u.setCreado_en(creadoTs.toLocalDateTime());
        }
        Timestamp actualizadoEnTimestamp = rs.getTimestamp("actualizado_en");
        if (actualizadoEnTimestamp != null) {
            u.setActualizado_en(actualizadoEnTimestamp.toLocalDateTime());
        }
        u.setNumero_cuenta(rs.getString("numero_cuenta"));
        u.setTitular_usuario(rs.getString("titular_cuenta"));
        return u;
    }
    public void updatePassword(int idUsuario, String nuevaContrasena) throws SQLException {
        String sql = "UPDATE USUARIO SET contrasena = ?, actualizado_en = CURRENT_TIMESTAMP WHERE id_usuario = ?";
        try (Connection conn = ConfigDB.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevaContrasena);
            pstmt.setInt(2, idUsuario);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo actualizar la contraseña, usuario no encontrado.");
            }
        }
    }
}