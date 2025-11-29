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

            // Validar y obtener `titulo_publicacion`
            String titulo = ctx.formParam("titulo_publicacion");
            System.out.println(ctx.formParam("titulo_publicacion"));
            if (titulo == null || titulo.isEmpty()) {
                ctx.status(400).result("El título de la publicación no puede ser nulo o vacío.");
                return;
            }
            publicacion.setTitulo_publicacion(titulo);

            // Validar y obtener `descripcion_publicacion`
            String descripcion = ctx.formParam("descripcion_publicacion");
            publicacion.setDescripcion_publicacion(descripcion);

            // Validar y obtener `id_vendedor`
            String idVendedorStr = ctx.formParam("id_vendedor");
            if (idVendedorStr == null || idVendedorStr.isEmpty()) {
                ctx.status(400).result("El ID del vendedor no puede ser nulo o vacío.");
                return;
            }
            publicacion.setId_vendedor(Integer.parseInt(idVendedorStr));

            // Validar y obtener `precio_producto`
            String precioStr = ctx.formParam("precio_producto");
            if (precioStr == null || precioStr.isEmpty()) {
                ctx.status(400).result("El precio del producto no puede ser nulo o vacío.");
                return;
            }
            publicacion.setPrecio_producto(Float.parseFloat(precioStr));

            // Resto del código...

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