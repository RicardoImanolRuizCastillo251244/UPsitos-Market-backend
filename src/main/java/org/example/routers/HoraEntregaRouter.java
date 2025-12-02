package org.example.routers;

import io.javalin.Javalin;
import org.example.controllers.HoraEntregaController;

public class HoraEntregaRouter {

    private final HoraEntregaController horaEntregaController;

    public HoraEntregaRouter(HoraEntregaController horaEntregaController) {
        this.horaEntregaController = horaEntregaController;
    }

    public void register(Javalin app) {
        String basePath = "/horaEntrega";
        app.post(basePath, horaEntregaController::save);
        app.get(basePath, horaEntregaController::findAll);
        app.get(basePath+ "/{id}", horaEntregaController::findById);
        app.delete(basePath+ "/{id}", horaEntregaController::delete);
    }
}
