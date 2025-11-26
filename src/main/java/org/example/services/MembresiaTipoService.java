package org.example.services;

import org.example.models.MembresiaTipo;
import org.example.repositories.MembresiaTipoRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MembresiaTipoService {
    private final MembresiaTipoRepository membresiaTipoRepository;

    public MembresiaTipoService(MembresiaTipoRepository membresiaTipoRepository) {
        this.membresiaTipoRepository = membresiaTipoRepository;
    }

    private void validateMembresiaTipo(MembresiaTipo membresiaTipo) {
        if (membresiaTipo.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        if (membresiaTipo.getDescripcion() == null || membresiaTipo.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
    }

    public MembresiaTipo save(MembresiaTipo membresiaTipo) throws Exception {
        validateMembresiaTipo(membresiaTipo);
        try {
            return membresiaTipoRepository.save(membresiaTipo);
        } catch (SQLException e) {
            throw new Exception("Error al guardar el tipo de membresía: " + e.getMessage(), e);
        }
    }

    public void update(MembresiaTipo membresiaTipo) throws Exception {
        if (membresiaTipo.getId_membresia_tipo() <= 0) {
            throw new IllegalArgumentException("El ID del tipo de membresía no es válido.");
        }
        validateMembresiaTipo(membresiaTipo);
        try {
            membresiaTipoRepository.update(membresiaTipo);
        } catch (SQLException e) {
            throw new Exception("Error al actualizar el tipo de membresía: " + e.getMessage(), e);
        }
    }

    public void deleteById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID no es válido.");
        membresiaTipoRepository.deleteById(id);
    }

    public Optional<MembresiaTipo> findById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID no es válido.");
        return membresiaTipoRepository.findById(id);
    }

    public List<MembresiaTipo> findAll() throws Exception {
        return membresiaTipoRepository.findAll();
    }
}