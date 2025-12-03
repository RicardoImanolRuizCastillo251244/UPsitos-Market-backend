package org.example.routers;

import io.javalin.Javalin;
import org.example.controllers.NotificacionController;

public class NotificacionRouter {
    private final NotificacionController notificacionController;

    public NotificacionRouter(NotificacionController notificacionController) {
        this.notificacionController = notificacionController;
    }

    public void register(Javalin app) {
        String basePath = "/notificacion";
        app.post(basePath, notificacionController::saveNotificacion);
        app.get(basePath, notificacionController::getAllNotificaciones);
        app.get(basePath + "/{id}", notificacionController::getNotificacionById);
        app.put(basePath + "/{id}", notificacionController::updateNotificacion);
        app.put(basePath + "/{id}/leer", notificacionController::marcarComoLeida);
        app.delete(basePath + "/{id}", notificacionController::deleteNotificacion);
        app.get(basePath + "/usuario/{id_usuario}", notificacionController::getNotificacionesByUsuario);
    }
}