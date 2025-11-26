package org.example.controllers;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.models.QuejaVenta;
import org.example.services.QuejaVentaService;

import java.util.Optional;

public class QuejaVentaController {
    private final QuejaVentaService quejaService;

    public QuejaVentaController(QuejaVentaService quejaService) {
        this.quejaService = quejaService;
    }

    private int parseId(String idStr) {
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El ID debe ser un número válido.");
        }
    }

    public void saveQuejaVenta(Context ctx) {
        try {
            // Crear objeto QuejaVenta manualmente desde form params
            QuejaVenta queja = new QuejaVenta();
            queja.setId_venta(Integer.parseInt(ctx.formParam("id_venta")));
            queja.setId_emisor(Integer.parseInt(ctx.formParam("id_emisor")));
            queja.setDescripcion_queja(ctx.formParam("descripcion_queja"));
            queja.setTipo_problema(ctx.formParam("tipo_problema")); // Opcional

            // Manejo de la imagen opcional
            UploadedFile uploadedFile = ctx.uploadedFile("imagen");
            if (uploadedFile != null) {
                String contentType = uploadedFile.contentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    ctx.status(400).result("El archivo debe ser una imagen.");
                    return;
                }
                byte[] imagenBytes = uploadedFile.content().readAllBytes();
                if (imagenBytes.length > 5 * 1024 * 1024) {
                    ctx.status(400).result("La imagen no debe superar los 5MB.");
                    return;
                }
                queja.setImagen(imagenBytes);
            }

            QuejaVenta savedQueja = quejaService.save(queja);
            ctx.status(201).json(savedQueja);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al procesar la queja de venta: " + e.getMessage());
        }
    }

    public void updateQuejaVenta(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id"));
            QuejaVenta queja = ctx.bodyAsClass(QuejaVenta.class);
            queja.setId(id);
            quejaService.update(queja);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al actualizar la queja de venta: " + e.getMessage());
        }
    }

    public void deleteQueja(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id"));
            quejaService.deleteById(id);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al eliminar la queja de venta: " + e.getMessage());
        }
    }

    public void getQuejaById(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id"));
            quejaService.findById(id)
                    .ifPresentOrElse(
                            queja -> ctx.status(200).json(queja),
                            () -> ctx.status(404).result("Queja de venta no encontrada.")
                    );
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al buscar la queja de venta: " + e.getMessage());
        }
    }

    public void getAllQuejas(Context ctx) {
        try {
            ctx.status(200).json(quejaService.findAll());
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener las quejas de venta: " + e.getMessage());
        }
    }
}