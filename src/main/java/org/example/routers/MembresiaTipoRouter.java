package org.example.routers;

import io.javalin.Javalin;
import org.example.controllers.MembresiaTipoController;

public class MembresiaTipoRouter {
    private final MembresiaTipoController membresiaTipoController;

    public MembresiaTipoRouter(MembresiaTipoController membresiaTipoController) {
        this.membresiaTipoController = membresiaTipoController;
    }

    public void register(Javalin app) {
        String basePath = "/membresia-tipo";
        app.post(basePath, membresiaTipoController::saveMembresiaTipo);
        app.get(basePath, membresiaTipoController::getAllMembresiaTipos);
        app.get(basePath + "/{id}", membresiaTipoController::getMembresiaTipoById);
        app.put(basePath + "/{id}", membresiaTipoController::updateMembresiaTipo);
        app.delete(basePath + "/{id}", membresiaTipoController::deleteMembresiaTipo);
    }
}