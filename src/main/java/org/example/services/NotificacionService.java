package org.example.services;

import org.example.models.Notificacion;
import org.example.repositories.NotificacionRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class NotificacionService {
    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    private void validateNotificacion(Notificacion notificacion) {
        if (notificacion.getId_usuario() <= 0) {
            throw new IllegalArgumentException("El ID de usuario no es válido.");
        }
        if (notificacion.getMensaje() == null || notificacion.getMensaje().trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío.");
        }
        if (notificacion.getTipo() == null || notificacion.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de notificación no puede estar vacío.");
        }
    }

    public Notificacion save(Notificacion notificacion) throws Exception {
        validateNotificacion(notificacion);
        try {
            return notificacionRepository.save(notificacion);
        } catch (SQLException e) {
            throw new Exception("Error al guardar la notificación: " + e.getMessage(), e);
        }
    }

    public void update(Notificacion notificacion) throws Exception {
        if (notificacion.getId() <= 0) {
            throw new IllegalArgumentException("El ID de la notificación no es válido.");
        }
        validateNotificacion(notificacion);
        notificacionRepository.update(notificacion);
    }

    public void deleteById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID no es válido.");
        notificacionRepository.deleteById(id);
    }

    public Optional<Notificacion> findById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID no es válido.");
        return notificacionRepository.findById(id);
    }

    public List<Notificacion> findAll() throws Exception {
        return notificacionRepository.findAll();
    }

    public List<Notificacion> findByUserId(int idUsuario) throws Exception {
        if (idUsuario <= 0) throw new IllegalArgumentException("El ID de usuario no es válido.");
        return notificacionRepository.findByUserId(idUsuario);
    }
}