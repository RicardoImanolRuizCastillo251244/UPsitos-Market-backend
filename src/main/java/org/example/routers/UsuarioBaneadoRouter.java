package org.example.routers;

import io.javalin.Javalin;
import org.example.controllers.UsuarioBaneadoController;

public class UsuarioBaneadoRouter {
    private final UsuarioBaneadoController ubController;

    public UsuarioBaneadoRouter(UsuarioBaneadoController ubController) {
        this.ubController = ubController;
    }

    public void register(Javalin app) {
        String basePath = "/usuario-baneado";
        app.post(basePath, ubController::banearUsuario);
        app.get(basePath, ubController::getAllBaneados);
        app.get(basePath + "/{id_usuario}", ubController::getBaneadoById);
        app.delete(basePath + "/{id_usuario}", ubController::desbanearUsuario);
    }
}