package org.example.services;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.models.Notificacion;
import org.example.models.QuejaUsuario;
import org.example.models.QuejaUsuarioDTO;
import org.example.models.Usuario;
import org.example.repositories.NotificacionRepository;
import org.example.repositories.QuejaUsuarioRepository;
import org.example.repositories.UsuarioRepository;

public class QuejaUsuarioService {
    private final QuejaUsuarioRepository quejaRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionRepository notificacionRepository;

    public QuejaUsuarioService(QuejaUsuarioRepository quejaRepository, UsuarioRepository usuarioRepository, NotificacionRepository notificacionRepository) {
        this.quejaRepository = quejaRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionRepository = notificacionRepository;
    }

    private void validateQueja(QuejaUsuario queja) {
        if (queja.getId_emisor() <= 0 || queja.getId_receptor() <= 0) {
            throw new IllegalArgumentException("Los IDs de emisor y receptor no son válidos.");
        }
        if (queja.getId_emisor() == queja.getId_receptor()) {
            throw new IllegalArgumentException("Un usuario no puede presentar una queja contra sí mismo.");
        }
        if (queja.getDescripcion_queja() == null || queja.getDescripcion_queja().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la queja no puede estar vacía.");
        }
    }

    public QuejaUsuario save(QuejaUsuario queja) throws Exception {
        validateQueja(queja);
        if (queja.getEstado_queja() == null) {
            queja.setEstado_queja("ABIERTA");
        }
        try {
            QuejaUsuario quejaGuardada = quejaRepository.save(queja);
            
            // Notificar a todos los administradores
            List<Usuario> admins = usuarioRepository.findAdmins();
            String tipoQueja = (queja.getId_publicacion() > 0) ? "Publicación" : "Usuario";
            String mensaje = "Nueva queja recibida sobre " + tipoQueja + ". Revisar inmediatamente.";
            
            for (Usuario admin : admins) {
                Notificacion notificacion = new Notificacion();
                notificacion.setId_usuario(admin.getId_usuario());
                notificacion.setMensaje(mensaje);
                notificacion.setTipo("QUEJA_USUARIO");
                notificacion.setLeida(false);
                notificacion.setFecha_envio(LocalDateTime.now());
                notificacionRepository.save(notificacion);
            }
            
            return quejaGuardada;
        } catch (SQLException e) {
            throw new Exception("Error al guardar la queja: " + e.getMessage(), e);
        }
    }

    public void update(QuejaUsuario queja) throws Exception {
        if (queja.getId() <= 0) {
            throw new IllegalArgumentException("El ID de la queja no es válido.");
        }
        validateQueja(queja);
        quejaRepository.update(queja);
    }

    public void deleteById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID no es válido.");
        quejaRepository.deleteById(id);
    }

    public Optional<QuejaUsuario> findById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID no es válido.");
        return quejaRepository.findById(id);
    }

    public List<QuejaUsuario> findAll() throws Exception {
        return quejaRepository.findAll();
    }

    public List<QuejaUsuarioDTO> findAllWithDetails() throws Exception {
        try {
            return quejaRepository.findAllWithDetails();
        } catch (SQLException e) {
            throw new Exception("Error al obtener las quejas con detalles: " + e.getMessage(), e);
        }
    }
}