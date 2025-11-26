package org.example.controllers;

import io.javalin.http.Context;
import org.example.models.Categoria;
import org.example.services.CategoriaService;

public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    private int parseId(String idStr) {
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El ID debe ser un número válido.");
        }
    }

    public void saveCategoria(Context ctx) {
        try {
            Categoria categoria = ctx.bodyAsClass(Categoria.class);
            Categoria savedCategoria = categoriaService.save(categoria);
            ctx.status(201).json(savedCategoria);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al guardar la categoría: " + e.getMessage());
        }
    }

    public void updateCategoria(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id"));
            Categoria categoria = ctx.bodyAsClass(Categoria.class);
            categoria.setId_categoria(id);
            categoriaService.update(categoria);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al actualizar la categoría: " + e.getMessage());
        }
    }

    public void deleteCategoria(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id"));
            categoriaService.deleteById(id);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al eliminar la categoría: " + e.getMessage());
        }
    }

    public void getCategoriaById(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id"));
            categoriaService.findById(id)
                    .ifPresentOrElse(
                            categoria -> ctx.status(200).json(categoria),
                            () -> ctx.status(404).result("Categoría no encontrada.")
                    );
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al buscar la categoría: " + e.getMessage());
        }
    }

    public void getAllCategorias(Context ctx) {
        try {
            ctx.status(200).json(categoriaService.findAll());
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener las categorías: " + e.getMessage());
        }
    }
}