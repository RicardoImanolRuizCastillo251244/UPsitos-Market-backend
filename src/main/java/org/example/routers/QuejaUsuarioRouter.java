package org.example.routers;

import io.javalin.Javalin;
import org.example.controllers.QuejaUsuarioController;

public class QuejaUsuarioRouter {
    private final QuejaUsuarioController quejaController;

    public QuejaUsuarioRouter(QuejaUsuarioController quejaController) {
        this.quejaController = quejaController;
    }

    public void register(Javalin app) {
        String basePath = "/queja-usuario";
        app.post(basePath, quejaController::saveQuejaUsuario);
        app.get(basePath, quejaController::getAllQuejas);
        app.get(basePath + "/{id}", quejaController::getQuejaById);
        app.put(basePath + "/{id}", quejaController::updateQuejaUsuario);
        app.delete(basePath + "/{id}", quejaController::deleteQueja);
    }
}