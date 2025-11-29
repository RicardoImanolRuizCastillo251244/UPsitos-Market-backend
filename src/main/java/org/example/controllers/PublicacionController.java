package org.example.controllers;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.example.models.Publicacion;
import org.example.services.PublicacionService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.javalin.http.Context;

public class PublicacionController {
    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    public void savePublicacion(Context ctx) {
        try {
            Publicacion publicacion;
            String contentType = ctx.contentType();

            if (contentType != null && contentType.contains("application/json")) {
                // Si el cliente envía JSON, procesar con ObjectMapper para tratar
                // foto_publicacion = "" como nulo y validar base64.
                ObjectMapper mapper = new ObjectMapper();
                String body = ctx.body();
                JsonNode root = mapper.readTree(body);
                if (root != null && root.isObject()) {
                    ObjectNode obj = (ObjectNode) root;
                    JsonNode fotoNode = obj.get("foto_publicacion");
                    if (fotoNode != null && fotoNode.isTextual()) {
                        String fotoStr = fotoNode.asText();
                        if (fotoStr == null || fotoStr.isEmpty()) {
                            obj.remove("foto_publicacion");
                        } else {
                            try {
                                byte[] decoded = java.util.Base64.getDecoder().decode(fotoStr);
                                obj.set("foto_publicacion", mapper.getNodeFactory().binaryNode(decoded));
                            } catch (IllegalArgumentException ex) {
                                ctx.status(400).result("El campo foto_publicacion no es una cadena Base64 válida.");
                                return;
                            }
                        }
                    }
                    publicacion = mapper.treeToValue(obj, Publicacion.class);
                } else {
                    publicacion = mapper.treeToValue(root, Publicacion.class);
                }
            } else {
                // Crear objeto Publicacion manualmente desde form params
                publicacion = new Publicacion();

                // Validar y obtener `titulo_publicacion`
                String titulo = ctx.formParam("titulo_publicacion");
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

                // id_categoria (opcional)
                String idCategoriaStr = ctx.formParam("id_categoria");
                if (idCategoriaStr != null && !idCategoriaStr.isEmpty()) {
                    publicacion.setId_categoria(Integer.parseInt(idCategoriaStr));
                }

                // existencia (opcional)
                String existenciaStr = ctx.formParam("existencia");
                if (existenciaStr != null && !existenciaStr.isEmpty()) {
                    publicacion.setExistencia(Integer.parseInt(existenciaStr));
                } else {
                    publicacion.setExistencia(1);
                }

                // foto (opcional) -- no se procesa el InputStream aquí
                // queda nula si no se envía
            }

            // Validaciones comunes después de tener el objeto (JSON o form)
            if (publicacion.getTitulo_publicacion() == null || publicacion.getTitulo_publicacion().isEmpty()) {
                ctx.status(400).result("El título de la publicación no puede ser nulo o vacío.");
                return;
            }
            if (publicacion.getId_vendedor() == 0) {
                ctx.status(400).result("El ID del vendedor no puede ser nulo o vacío.");
                return;
            }
            if (publicacion.getPrecio_producto() == 0.0f) {
                ctx.status(400).result("El precio del producto no puede ser nulo o vacío.");
                return;
            }

            // asignar valores por defecto cuando faltan
            if (publicacion.getEstado_publicacion() == null) {
                publicacion.setEstado_publicacion("ACTIVA");
            }
            LocalDateTime ahora = LocalDateTime.now();
            if (publicacion.getFecha_publicacion() == null) {
                publicacion.setFecha_publicacion(ahora);
            }
            if (publicacion.getFecha_expiracion() == null) {
                publicacion.setFecha_expiracion(ahora.plusDays(7));
            }
            if (publicacion.getExistencia() == 0) {
                publicacion.setExistencia(1);
            }

            // Guardar usando el servicio
            Publicacion saved = publicacionService.savePublicacion(publicacion);
            ctx.status(201).json(saved);

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