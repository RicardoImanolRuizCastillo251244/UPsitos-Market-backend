package org.example.routers;

import io.javalin.Javalin;
import org.example.controllers.QuejaVentaController;

public class QuejaVentaRouter {
    private final QuejaVentaController quejaController;

    public QuejaVentaRouter(QuejaVentaController quejaController) {
        this.quejaController = quejaController;
    }

    public void register(Javalin app) {
        String basePath = "/queja-venta";
        app.post(basePath, quejaController::saveQuejaVenta);
        app.get(basePath, quejaController::getAllQuejas);
        app.get(basePath + "/{id}", quejaController::getQuejaById);
        app.put(basePath + "/{id}", quejaController::updateQuejaVenta);
        app.delete(basePath + "/{id}", quejaController::deleteQueja);
    }
}