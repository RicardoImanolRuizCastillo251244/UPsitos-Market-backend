package org.example.routers;

import io.javalin.Javalin;
import org.example.controllers.CategoriaController;

public class CategoriaRouter {
    private final CategoriaController categoriaController;

    public CategoriaRouter(CategoriaController categoriaController) {
        this.categoriaController = categoriaController;
    }

    public void register(Javalin app) {
        String basePath = "/categoria";
        app.post(basePath, categoriaController::saveCategoria);
        app.get(basePath, categoriaController::getAllCategorias);
        app.get(basePath + "/{id}", categoriaController::getCategoriaById);
        app.put(basePath + "/{id}", categoriaController::updateCategoria);
        app.delete(basePath + "/{id}", categoriaController::deleteCategoria);
    }
}