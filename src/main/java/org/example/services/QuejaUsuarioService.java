package org.example.services;

import org.example.models.QuejaUsuario;
import org.example.models.QuejaUsuarioDTO;
import org.example.repositories.QuejaUsuarioRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class QuejaUsuarioService {
    private final QuejaUsuarioRepository quejaRepository;

    public QuejaUsuarioService(QuejaUsuarioRepository quejaRepository) {
        this.quejaRepository = quejaRepository;
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
            return quejaRepository.save(queja);
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