package org.example.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.example.models.Publicacion;
import org.example.services.PublicacionService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

public class PublicacionController {
    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    public void savePublicacion(Context ctx) {
        Publicacion publicacion;
        String mediaType = ctx.contentType();

        try {
            if (mediaType != null && mediaType.contains("application/json")) {
                ObjectMapper mapper = new ObjectMapper();
                String body = ctx.body();
                JsonNode root = mapper.readTree(body);
                if (root != null && root.isObject()) {
                    ObjectNode obj = (ObjectNode) root;

                    // foto_publicacion: si viene como Base64 string la decodificamos
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
                                ctx.status(400).result("El campo foto_publicacion no es Base64 válido.");
                                return;
                            }
                        }
                    }

                    // Normalizar campos numéricos que pueden llegar como cadena vacía
                    String[] numericFields = new String[]{"id_vendedor", "precio_producto", "id_categoria", "existencia"};
                    for (String nf : numericFields) {
                        JsonNode nnode = obj.get(nf);
                        if (nnode != null && nnode.isTextual()) {
                            String s = nnode.asText();
                            if (s == null || s.isEmpty()) {
                                obj.remove(nf);
                            } else {
                                try {
                                    if ("precio_producto".equals(nf)) {
                                        double v = Double.parseDouble(s);
                                        obj.put(nf, v);
                                    } else {
                                        int v = Integer.parseInt(s);
                                        obj.put(nf, v);
                                    }
                                } catch (NumberFormatException ex) {
                                    ctx.status(400).result("El campo " + nf + " debe ser numérico.");
                                    return;
                                }
                            }
                        }
                    }

                    publicacion = mapper.treeToValue(obj, Publicacion.class);
                } else {
                    publicacion = new Publicacion();
                }

            } else {
                // form-data
                publicacion = new Publicacion();

                String titulo = ctx.formParam("titulo_publicacion");
                if (titulo == null || titulo.isEmpty()) {
                    ctx.status(400).result("El título de la publicación no puede ser nulo o vacío.");
                    return;
                }
                publicacion.setTitulo_publicacion(titulo);
                publicacion.setDescripcion_publicacion(ctx.formParam("descripcion_publicacion"));
                String precioStr = ctx.formParam("precio_producto");
                if (precioStr != null && !precioStr.isEmpty()) {
                    try {
                        publicacion.setPrecio_producto(Float.parseFloat(precioStr));
                    } catch (NumberFormatException ex) {
                        ctx.status(400).result("El campo precio_producto debe ser numérico.");
                        return;
                    }
                }

                String idVendedorStr = ctx.formParam("id_vendedor");
                if (idVendedorStr != null && !idVendedorStr.isEmpty()) {
                    try {
                        publicacion.setId_vendedor(Integer.parseInt(idVendedorStr));
                    } catch (NumberFormatException ex) {
                        ctx.status(400).result("El campo id_vendedor debe ser numérico.");
                        return;
                    }
                }
                String idCategoriaStr = ctx.formParam("id_categoria");
                if (idCategoriaStr != null && !idCategoriaStr.isEmpty()) {
                    try {
                        publicacion.setId_categoria(Integer.parseInt(idCategoriaStr));
                    } catch (NumberFormatException ex) {
                        ctx.status(400).result("El campo id_categoria debe ser numérico.");
                        return;
                    }
                }

                UploadedFile uploadedFile = ctx.uploadedFile("foto_publicacion");
                if (uploadedFile != null) {
                    String fileContentType = uploadedFile.contentType();
                    if (fileContentType == null || !fileContentType.startsWith("image/")) {
                        ctx.status(400).result("El archivo debe ser una imagen.");
                        return;
                    }

                    byte[] imagenBytes;
                    try (InputStream is = uploadedFile.content()) {
                        imagenBytes = is.readAllBytes();
                    } catch (IOException ex) {
                        ctx.status(500).result("Error leyendo la imagen: " + ex.getMessage());
                        return;
                    }

                    if (imagenBytes.length > 5 * 1024 * 1024) {
                        ctx.status(400).result("La imagen no debe superar los 5MB.");
                        return;
                    }

                    publicacion.setFoto_publicacion(imagenBytes);
                }

                String existenciaStr = ctx.formParam("existencia");
                if (existenciaStr != null && !existenciaStr.isEmpty()) {
                    try {
                        publicacion.setExistencia_publicacion(Integer.parseInt(existenciaStr));
                    } catch (NumberFormatException ex) {
                        ctx.status(400).result("El campo existencia debe ser numérico.");
                        return;
                    }
                }
            }

            if (publicacion == null) {
                ctx.status(400).result("Datos de publicación inválidos.");
                return;
            }

            // Guardar publicación
            Publicacion saved = publicacionService.savePublicacion(publicacion);
            ctx.status(201).json(saved);

        } catch (JsonProcessingException e) {
            ctx.status(400).result("JSON inválido: " + e.getMessage());
        } catch (SQLException e) {
            ctx.status(500).result("Error de base de datos: " + e.getMessage());
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

    public void updateEstadoPublicacion(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        String nuevoEstado = ctx.body();
        
        // Obtener la publicación actual
        publicacionService.getPublicacionById(id).ifPresentOrElse(pub -> {
            try {
                pub.setEstado_publicacion(nuevoEstado);
                publicacionService.updatePublicacion(pub);
                ctx.status(200).json(pub);
            } catch (SQLException e) {
                ctx.status(500).result("Error al actualizar estado: " + e.getMessage());
            }
        }, () -> ctx.status(404).result("Publicación no encontrada"));
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