package org.example.routers;

import io.javalin.Javalin;
import org.example.controllers.CalificacionController;

public class CalificacionRouter {
    private final CalificacionController calificacionController;

    public CalificacionRouter(CalificacionController calificacionController) {
        this.calificacionController = calificacionController;
    }

    public void register(Javalin app) {
        String basePath = "/calificacion";
        app.post(basePath, calificacionController::saveCalificacion);
        app.get(basePath, calificacionController::getAllCalificaciones);
        app.get(basePath + "/{id}", calificacionController::getCalificacionById);
        app.put(basePath + "/{id}", calificacionController::updateCalificacion);
        app.delete(basePath + "/{id}", calificacionController::deleteCalificacion);
        app.get(basePath + "/promedio/{id_publicacion}", calificacionController::getAverageRating);
    }
}