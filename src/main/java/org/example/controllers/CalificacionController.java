package org.example.controllers;

import io.javalin.http.Context;
import org.example.models.Calificacion;
import org.example.services.CalificacionService;

import java.util.HashMap;
import java.util.Map;

public class CalificacionController {
    private final CalificacionService calificacionService;

    public CalificacionController(CalificacionService calificacionService) {
        this.calificacionService = calificacionService;
    }

    private int parseId(String idStr) {
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El ID debe ser un número válido.");
        }
    }

    public void saveCalificacion(Context ctx) {
        try {
            Calificacion calificacion = ctx.bodyAsClass(Calificacion.class);
            Calificacion savedCalificacion = calificacionService.save(calificacion);
            ctx.status(201).json(savedCalificacion);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al procesar la calificación: " + e.getMessage());
        }
    }

    public void updateCalificacion(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id"));
            Calificacion calificacion = ctx.bodyAsClass(Calificacion.class);
            calificacion.setId_calificacion(id);
            calificacionService.update(calificacion);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al actualizar la calificación: " + e.getMessage());
        }
    }

    public void deleteCalificacion(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id"));
            calificacionService.deleteById(id);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al eliminar la calificación: " + e.getMessage());
        }
    }

    public void getCalificacionById(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id"));
            calificacionService.findById(id)
                    .ifPresentOrElse(
                            calificacion -> ctx.status(200).json(calificacion),
                            () -> ctx.status(404).result("Calificación no encontrada.")
                    );
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al buscar la calificación: " + e.getMessage());
        }
    }

    public void getAllCalificaciones(Context ctx) {
        try {
            ctx.status(200).json(calificacionService.findAll());
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener las calificaciones: " + e.getMessage());
        }
    }

    public void getAverageRating(Context ctx) {
        System.out.println("Path params: " + ctx.pathParamMap());
        try {
            int idPublicacion = parseId(ctx.pathParam("id_publicacion"));
            Float promedio = calificacionService.getAverageRatingByPublicationId(idPublicacion).orElse(null);
            Map<String, Object> json = new HashMap<>();
            json.put("promedio", promedio);
            ctx.status(200).json(json);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al calcular el promedio de la calificación: " + e.getMessage());
        }
    }
}