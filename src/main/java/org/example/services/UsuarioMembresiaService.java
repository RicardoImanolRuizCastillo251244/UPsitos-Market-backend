package org.example.services;

import org.example.models.UsuarioMembresia;
import org.example.repositories.UsuarioMembresiaRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UsuarioMembresiaService {
    private final UsuarioMembresiaRepository umRepository;

    public UsuarioMembresiaService(UsuarioMembresiaRepository umRepository) {
        this.umRepository = umRepository;
    }

    private void validateUsuarioMembresia(UsuarioMembresia um) {
        if (um.getId_usuario() <= 0) {
            throw new IllegalArgumentException("El ID de usuario no es válido.");
        }
        if (um.getId_membresia_tipo() <= 0) {
            throw new IllegalArgumentException("El ID de tipo de membresía no es válido.");
        }
        if (um.getFecha_inicio() != null && um.getFecha_expiracion() != null && um.getFecha_expiracion().isBefore(um.getFecha_inicio())) {
            throw new IllegalArgumentException("La fecha de expiración no puede ser anterior a la fecha de inicio.");
        }
    }

    public UsuarioMembresia save(UsuarioMembresia um) throws Exception {
        if (um.getFecha_inicio() == null) {
            um.setFecha_inicio(LocalDateTime.now());
        }
        validateUsuarioMembresia(um);
        try {
            return umRepository.save(um);
        } catch (SQLException e) {
            throw new Exception("Error al guardar la membresía de usuario: " + e.getMessage(), e);
        }
    }

    public void update(UsuarioMembresia um) throws Exception {
        if (um.getId_usuario_membresia() <= 0) {
            throw new IllegalArgumentException("El ID de la membresía de usuario no es válido.");
        }
        validateUsuarioMembresia(um);
        try {
            umRepository.update(um);
        } catch (SQLException e) {
            throw new Exception("Error al actualizar la membresía de usuario: " + e.getMessage(), e);
        }
    }

    public void deleteById(int id) throws Exception {
        umRepository.deleteById(id);
    }

    public Optional<UsuarioMembresia> findById(int id) throws Exception {
        return umRepository.findById(id);
    }

    public List<UsuarioMembresia> findAll() throws Exception {
        return umRepository.findAll();
    }
}