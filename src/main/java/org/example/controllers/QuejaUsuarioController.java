package org.example.controllers;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.models.QuejaUsuario;
import org.example.services.QuejaUsuarioService;

import java.util.Optional;

public class QuejaUsuarioController {
    private final QuejaUsuarioService quejaService;

    public QuejaUsuarioController(QuejaUsuarioService quejaService) {
        this.quejaService = quejaService;
    }

    private int parseId(String idStr) {
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El ID debe ser un número válido.");
        }
    }

    public void saveQuejaUsuario(Context ctx) {
        try {
            // Crear objeto QuejaUsuario manualmente desde form params
            QuejaUsuario queja = new QuejaUsuario();
            queja.setId_emisor(Integer.parseInt(ctx.formParam("id_emisor")));
            queja.setId_receptor(Integer.parseInt(ctx.formParam("id_receptor")));
            queja.setDescripcion_queja(ctx.formParam("descripcion_queja"));
            queja.setMotivo_queja(ctx.formParam("motivo_queja")); // Opcional

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

            QuejaUsuario savedQueja = quejaService.save(queja);
            ctx.status(201).json(savedQueja);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al procesar la queja: " + e.getMessage());
        }
    }

    public void updateQuejaUsuario(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id"));
            QuejaUsuario queja = ctx.bodyAsClass(QuejaUsuario.class);
            queja.setId(id);
            quejaService.update(queja);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al actualizar la queja: " + e.getMessage());
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
            ctx.status(500).result("Error al eliminar la queja: " + e.getMessage());
        }
    }

    public void getQuejaById(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id"));
            quejaService.findById(id)
                    .ifPresentOrElse(
                            queja -> ctx.status(200).json(queja),
                            () -> ctx.status(404).result("Queja no encontrada.")
                    );
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al buscar la queja: " + e.getMessage());
        }
    }

    public void getAllQuejas(Context ctx) {
        try {
            ctx.status(200).json(quejaService.findAll());
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener las quejas: " + e.getMessage());
        }
    }
}