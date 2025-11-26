package org.example.services;

import org.example.models.Calificacion;
import org.example.models.Publicacion;
import org.example.repositories.CalificacionRepository;
import org.example.repositories.PublicacionRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CalificacionService {
    private final CalificacionRepository calificacionRepository;
    private final PublicacionRepository publicacionRepository;

    public CalificacionService(CalificacionRepository calificacionRepository, PublicacionRepository publicacionRepository) {
        this.calificacionRepository = calificacionRepository;
        this.publicacionRepository = publicacionRepository;
    }

    private void validateCalificacion(Calificacion calificacion) throws Exception {
        if (calificacion.getId_usuario_califica() <= 0 || calificacion.getId_publicacion() <= 0) {
            throw new IllegalArgumentException("IDs de usuario o publicación no válidos.");
        }
        if (calificacion.getCalificacion() < 1 || calificacion.getCalificacion() > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5.");
        }

        Publicacion publicacion = publicacionRepository.findById(calificacion.getId_publicacion())
                .orElseThrow(() -> new IllegalArgumentException("La publicación no existe."));

        if (publicacion.getId_vendedor() == calificacion.getId_usuario_califica()) {
            throw new IllegalArgumentException("No puedes calificar tu propia publicación.");
        }

        if (calificacionRepository.existsByUsuarioAndPublicacion(calificacion.getId_usuario_califica(), calificacion.getId_publicacion())) {
            throw new IllegalArgumentException("Ya has calificado esta publicación.");
        }
    }

    public Calificacion save(Calificacion calificacion) throws Exception {
        validateCalificacion(calificacion);
        try {
            return calificacionRepository.save(calificacion);
        } catch (SQLException e) {
            throw new Exception("Error al guardar la calificación: " + e.getMessage(), e);
        }
    }

    public void update(Calificacion calificacion) throws Exception {
        if (calificacion.getId_calificacion() <= 0) {
            throw new IllegalArgumentException("El ID de la calificación no es válido.");
        }
        if (calificacion.getCalificacion() < 1 || calificacion.getCalificacion() > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5.");
        }
        calificacionRepository.update(calificacion);
    }

    public void deleteById(int id) throws Exception {
        calificacionRepository.deleteById(id);
    }

    public Optional<Calificacion> findById(int id) throws Exception {
        return calificacionRepository.findById(id);
    }

    public List<Calificacion> findAll() throws Exception {
        return calificacionRepository.findAll();
    }

    public Optional<Float> getAverageRatingByPublicationId(int idPublicacion) throws Exception {
        if (idPublicacion <= 0) throw new IllegalArgumentException("ID de publicación inválido.");
        return calificacionRepository.getAverageRatingByPublicationId(idPublicacion);
    }
}