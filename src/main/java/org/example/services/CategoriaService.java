package org.example.services;

import org.example.models.Categoria;
import org.example.repositories.CategoriaRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    private void validateCategoria(Categoria categoria) {
        if (categoria.getTipo() == null || categoria.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de categoría no puede estar vacío.");
        }
    }

    public Categoria save(Categoria categoria) throws Exception {
        validateCategoria(categoria);
        try {
            return categoriaRepository.save(categoria);
        } catch (SQLException e) {
            throw new Exception("Error al guardar la categoría: " + e.getMessage(), e);
        }
    }

    public void update(Categoria categoria) throws Exception {
        if (categoria.getId_categoria() <= 0) {
            throw new IllegalArgumentException("El ID de la categoría no es válido.");
        }
        validateCategoria(categoria);
        try {
            categoriaRepository.update(categoria);
        } catch (SQLException e) {
            throw new Exception("Error al actualizar la categoría: " + e.getMessage(), e);
        }
    }

    public void deleteById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID no es válido.");
        categoriaRepository.deleteById(id);
    }

    public Optional<Categoria> findById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID no es válido.");
        return categoriaRepository.findById(id);
    }

    public List<Categoria> findAll() throws Exception {
        return categoriaRepository.findAll();
    }
}