package org.example.services;

import org.example.models.LoginDTO;
import org.example.models.UpdateUsuarioDTO;
import org.example.models.Usuario;
import org.example.repositories.UsuarioRepository;
import com.password4j.Password;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\d{6}@.+\\.upchiapas\\.edu\\.mx$");

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    private void validateUsuario(Usuario usuario) {
        if (usuario.getId_rol() <= 0) {
            throw new IllegalArgumentException("El ID de rol no es válido.");
        }
        if (usuario.getNombre_usuario() == null || usuario.getNombre_usuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        if (usuario.getCorreo_usuario() == null || !EMAIL_PATTERN.matcher(usuario.getCorreo_usuario()).matches()) {
            throw new IllegalArgumentException("El correo electrónico no tiene el formato institucional válido (ej: 123456@carrera.upchiapas.edu.mx).");
        }
    }

    public List<Usuario> getUsuariosConMembresiaPremium() throws Exception {
        List<Usuario> usuarios = usuarioRepository.findUsuariosConMembresiaPremium();
        if (usuarios.isEmpty()) {
            throw new Exception("No se encontraron usuarios con membresía premium");
        }
        return usuarios;
    }

    public List<Usuario> getUsuariosConMembresiaSemiPremium() throws Exception {
        List<Usuario> usuarios = usuarioRepository.findUsuariosConMembresiaSemiPremium();
        if (usuarios.isEmpty()) {
            throw new Exception("No se encontraron usuarios con membresía semipremium");
        }
        return usuarios;
    }


    public Usuario registerUsuario(Usuario usuario, String plainPassword) throws Exception {
        if (plainPassword == null || plainPassword.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
        validateUsuario(usuario);
        usuario.setActivo(true);

        // Encriptar la contraseña
        String hashedPassword = Password.hash(plainPassword).withBcrypt().getResult();
        usuario.setContrasena(hashedPassword);

        try {
            return usuarioRepository.save(usuario);
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed") || e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("El correo electrónico ya está en uso.");
            }
            throw new Exception("Error al guardar el usuario: " + e.getMessage(), e);
        }
    }

    public void update(Usuario usuario, String plainPassword) throws Exception {
        if (usuario.getId_usuario() <= 0) {
            throw new IllegalArgumentException("El ID de usuario no es válido.");
        }
        validateUsuario(usuario);

        if (plainPassword != null && !plainPassword.isEmpty()) {
            System.out.println("La conytraseña es " + plainPassword);
            if (plainPassword.length() < 8) throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
            String hashedPassword = Password.hash(plainPassword).withBcrypt().getResult();
            usuario.setContrasena(hashedPassword);
        }

        try {
            usuarioRepository.update(usuario);
        } catch (SQLException e) {
            throw new Exception("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }

    public Usuario loginUsuario(String email, String password) throws Exception {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new LoginException("Credenciales inválidas."));

        if (!usuario.isActivo()) {
            throw new LoginException("La cuenta de usuario está inactiva.");
        }

        // Verificar la contraseña con Password4j
        boolean passwordVerified = Password.check(password, usuario.getContrasena()).withBcrypt();        if (passwordVerified) {
            return usuario;
        } else {
            throw new LoginException("Credenciales inválidas.");
        }
    }

    public void deleteById(int id) throws Exception {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> findById(int id) throws Exception {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> findAll() throws Exception {
        return usuarioRepository.findAll();
    }
    public void updateUsuarioParcial(int idUsuarioObjetivo, UpdateUsuarioDTO dto) throws Exception {
        Usuario usuario = usuarioRepository.findById(idUsuarioObjetivo)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));


        if (dto.getNombre_usuario() != null && !dto.getNombre_usuario().isEmpty()) {
            usuario.setNombre_usuario(dto.getNombre_usuario());
        }

        if (dto.getCorreo_usuario() != null && !dto.getCorreo_usuario().isEmpty()) {
            usuario.setCorreo_usuario(dto.getCorreo_usuario());
        }

        if (dto.getNumero_cuenta() != null) {
            usuario.setNumero_cuenta(dto.getNumero_cuenta());
        }

        if (dto.getTitular_usuario() != null) {
            usuario.setTitular_usuario(dto.getTitular_usuario());
        }

        if (dto.getContrasena() != null && !dto.getContrasena().isEmpty()) {
            if (dto.getContrasena().length() < 8) {
                throw new IllegalArgumentException("La nueva contraseña debe tener al menos 8 caracteres.");
            }
            String hashedPassword = Password.hash(dto.getContrasena()).withBcrypt().getResult();
            usuario.setContrasena(hashedPassword);
        }

        if (dto.getId_rol() != null && dto.getId_rol() > 0) {
            usuario.setId_rol(dto.getId_rol());
        }

        if (dto.isActivo() != null) {
            usuario.setActivo(dto.isActivo());
        }

        usuarioRepository.update(usuario);
    }
}