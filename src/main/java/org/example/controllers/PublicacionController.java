package org.example.controllers;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.models.Publicacion;
import org.example.services.PublicacionService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class PublicacionController {
    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    public void savePublicacion(Context ctx) {
        try {
            // Crear objeto Publicacion manualmente desde form params
            Publicacion publicacion = new Publicacion();
            publicacion.setTitulo_publicacion(ctx.formParam("titulo_publicacion"));
            publicacion.setDescripcion_publicacion(ctx.formParam("descripcion_publicacion"));
            publicacion.setId_vendedor(Integer.parseInt(ctx.formParam("id_vendedor")));
            publicacion.setPrecio_producto(Float.parseFloat(ctx.formParam("precio_producto")));

            // Parsear fecha de expiración
            String fechaExpiracionStr = ctx.formParam("fecha_expiracion");
            if (fechaExpiracionStr != null && !fechaExpiracionStr.isEmpty()) {
                publicacion.setFecha_expiracion(LocalDateTime.parse(fechaExpiracionStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }

            // Parsear id_categoria opcional
            String idCategoriaStr = ctx.formParam("id_categoria");
            if (idCategoriaStr != null && !idCategoriaStr.isEmpty()) {
                publicacion.setId_categoria(Integer.parseInt(idCategoriaStr));
            }

            // Manejo de la imagen opcional
            UploadedFile uploadedFile = ctx.uploadedFile("foto_publicacion");
            if (uploadedFile != null) {
                // Validar tipo MIME
                String contentType = uploadedFile.contentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    ctx.status(400).result("El archivo debe ser una imagen.");
                    return;
                }
                // Validar tamaño máximo (5MB)
                byte[] imagenBytes = uploadedFile.content().readAllBytes();
                if (imagenBytes.length > 5 * 1024 * 1024) {
                    ctx.status(400).result("La imagen no debe superar los 5MB.");
                    return;
                }
                publicacion.setFoto_publicacion(imagenBytes);
            }

            Publicacion savedPublicacion = publicacionService.savePublicacion(publicacion);
            ctx.status(201).json(savedPublicacion);
        } catch (NumberFormatException e) {
            ctx.status(400).result("Error de formato en uno de los campos numéricos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al guardar la publicación: " + e.getMessage());
        }
    }

    public void getPublicacionesOrderByPrecioAsc(Context ctx) {
        try {
            ctx.json(publicacionService.getPublicacionesOrderByPrecioAsc());
            ctx.status(200);
        } catch (Exception e) {
            ctx.status(404).result(e.getMessage());
        }
    }

    public void getPublicacionesOrderByPrecioDesc(Context ctx) {
        try {
            ctx.json(publicacionService.getPublicacionesOrderByPrecioDesc());
            ctx.status(200);
        } catch (Exception e) {
            ctx.status(404).result(e.getMessage());
        }
    }


    public void getAllPublicaciones(Context ctx) throws SQLException {
        ctx.json(publicacionService.getAllPublicaciones());
    }

    public void getPublicacionById(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        publicacionService.getPublicacionById(id)
                .ifPresentOrElse(ctx::json, () -> ctx.status(404));
    }

    public void updatePublicacion(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Publicacion publicacion = ctx.bodyAsClass(Publicacion.class);
        publicacion.setId_publicacion(id);
        publicacionService.updatePublicacion(publicacion);
        ctx.status(204);
    }

    public void deletePublicacion(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        publicacionService.deletePublicacion(id);
        ctx.status(204);
    }

    public void getPublicacionesByCategoria(Context ctx) throws SQLException {
        int idCategoria = Integer.parseInt(ctx.pathParam("id_categoria"));
        ctx.json(publicacionService.getPublicacionesByCategoria(idCategoria));
    }

    public void getPublicacionesByTitulo(Context ctx) throws SQLException {
        String titulo = ctx.queryParam("titulo");
        ctx.json(publicacionService.getPublicacionesByTitulo(titulo));
    }

    public void getPremiumPublications(Context ctx) {
        try {
            ctx.json(publicacionService.getPremiumSellerPublications());
        } catch (SQLException e) {
            ctx.status(500).result("Error de base de datos: " + e.getMessage());
        }
    }
}