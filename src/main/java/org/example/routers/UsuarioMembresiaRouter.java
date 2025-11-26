package org.example.routers;

import io.javalin.Javalin;
import org.example.controllers.UsuarioMembresiaController;

public class UsuarioMembresiaRouter {
    private final UsuarioMembresiaController umController;

    public UsuarioMembresiaRouter(UsuarioMembresiaController umController) {
        this.umController = umController;
    }

    public void register(Javalin app) {
        String basePath = "/usuario-membresia";
        app.post(basePath, umController::saveUsuarioMembresia);
        app.get(basePath, umController::getAllUsuarioMembresias);
        app.get(basePath + "/{id}", umController::getUsuarioMembresiaById);
        app.put(basePath + "/{id}", umController::updateUsuarioMembresia);
        app.delete(basePath + "/{id}", umController::deleteUsuarioMembresia);
    }
}