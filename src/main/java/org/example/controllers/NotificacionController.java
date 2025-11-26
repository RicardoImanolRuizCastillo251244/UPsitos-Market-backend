package org.example.controllers;

import io.javalin.http.Context;
import org.example.models.Notificacion;
import org.example.services.NotificacionService;

public class NotificacionController {
    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    private int parseId(String idStr) {
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El ID debe ser un número válido.");
        }
    }

    public void saveNotificacion(Context ctx) {
        try {
            Notificacion notificacion = ctx.bodyAsClass(Notificacion.class);
            Notificacion savedNotificacion = notificacionService.save(notificacion);
            ctx.status(201).json(savedNotificacion);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al guardar la notificación: " + e.getMessage());
        }
    }

    public void updateNotificacion(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id"));
            Notificacion notificacion = ctx.bodyAsClass(Notificacion.class);
            notificacion.setId(id);
            notificacionService.update(notificacion);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al actualizar la notificación: " + e.getMessage());
        }
    }

    public void deleteNotificacion(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id"));
            notificacionService.deleteById(id);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al eliminar la notificación: " + e.getMessage());
        }
    }

    public void getNotificacionById(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id"));
            notificacionService.findById(id)
                    .ifPresentOrElse(
                            notificacion -> ctx.status(200).json(notificacion),
                            () -> ctx.status(404).result("Notificación no encontrada.")
                    );
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al buscar la notificación: " + e.getMessage());
        }
    }

    public void getAllNotificaciones(Context ctx) {
        try {
            ctx.status(200).json(notificacionService.findAll());
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    public void getNotificacionesByUsuario(Context ctx) {
        try {
            int idUsuario = parseId(ctx.pathParam("id_usuario"));
            ctx.status(200).json(notificacionService.findByUserId(idUsuario));
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener las notificaciones del usuario: " + e.getMessage());
        }
    }
}