package org.example.routers;

import io.javalin.Javalin;
import org.example.controllers.PublicacionController;

public class PublicacionRouter {
    private final PublicacionController publicacionController;

    public PublicacionRouter(PublicacionController publicacionController) {
        this.publicacionController = publicacionController;
    }

    public void register(Javalin app) {
        String basePath = "/publicacion";
        app.post(basePath, publicacionController::savePublicacion);
        app.get(basePath, publicacionController::getAllPublicaciones);
        app.get(basePath + "/{id}", publicacionController::getPublicacionById);
        app.put(basePath + "/{id}", publicacionController::updatePublicacion);
        app.delete(basePath + "/{id}", publicacionController::deletePublicacion);
        app.get(basePath + "/categoria/{id_categoria}", publicacionController::getPublicacionesByCategoria);
        app.get(basePath + "/buscar", publicacionController::getPublicacionesByTitulo);
        app.get(basePath + "/premium", publicacionController::getPremiumPublications);
        app.get(basePath + "/precio-asc", publicacionController::getPublicacionesOrderByPrecioAsc);
        app.get(basePath + "/precio-desc", publicacionController::getPublicacionesOrderByPrecioDesc);

    }
}