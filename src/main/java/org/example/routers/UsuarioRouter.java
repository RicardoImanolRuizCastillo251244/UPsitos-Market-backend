package org.example.routers;

import io.javalin.Javalin;
import org.example.controllers.UsuarioController;

public class UsuarioRouter {
    private final UsuarioController usuarioController;

    public UsuarioRouter(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
    }

    public void register(Javalin app) {
        app.post("/auth/register", usuarioController::register);
        app.post("/auth/login", usuarioController::login);
        app.get("/usuario/profile", usuarioController::getProfile);
        app.put("/usuario/profile", usuarioController::updateUsuario);
        app.delete("/usuario/{id}", usuarioController::deleteUsuario);
        app.get("/usuario/{id}", usuarioController::getUsuarioById);
        app.get("/usuario", usuarioController::getAllUsuarios);
        app.get("/usuario/premium", usuarioController::getUsuariosConMembresiaPremium);
        app.get("/usuario/semi-premium", usuarioController::getUsuariosConMembresiaSemiPremium);
    }
}