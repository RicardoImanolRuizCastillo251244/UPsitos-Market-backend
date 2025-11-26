package org.example.services;

import org.example.models.Rol;
import org.example.repositories.RolRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RolService {
    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public Rol save(Rol rol) throws Exception {
        if (rol.getDescripcion() == null || rol.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del rol no puede estar vacía.");
        }
        try {
            return rolRepository.save(rol);
        } catch (SQLException e) {
            throw new Exception("Error al guardar el rol: " + e.getMessage(), e);
        }
    }

    public void update(Rol rol) throws Exception {
        if (rol.getId_rol() <= 0) {
            throw new IllegalArgumentException("El ID del rol no es válido.");
        }
        if (rol.getDescripcion() == null || rol.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del rol no puede estar vacía.");
        }
        try {
            rolRepository.update(rol);
        } catch (SQLException e) {
            throw new Exception("Error al actualizar el rol: " + e.getMessage(), e);
        }
    }

    public void deleteById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del rol no es válido.");
        }
        try {
            rolRepository.deleteById(id);
        } catch (SQLException e) {
            throw new Exception("Error al eliminar el rol: " + e.getMessage(), e);
        }
    }

    public Optional<Rol> findById(int id) throws Exception {
        return rolRepository.findById(id);
    }

    public List<Rol> findAll() throws Exception {
        return rolRepository.findAll();
    }
}