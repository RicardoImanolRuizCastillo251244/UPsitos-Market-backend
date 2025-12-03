package org.example.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.models.Publicacion;
import org.example.services.PublicacionService;

import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PublicacionController {
    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    public void savePublicacion(Context ctx) {
        try {
            Publicacion publicacion = null;
            String contentType = ctx.contentType();

            if (contentType != null && contentType.contains("application/json")) {
                ObjectMapper mapper = new ObjectMapper();
                String body = ctx.body();
                JsonNode root = mapper.readTree(body);
                if (root != null && root.isObject()) {
                    ObjectNode obj = (ObjectNode) root;

                    // foto_publicacion: tratar cadena vacía como nulo o decodificar base64
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

                    // Normalizar campos numéricos que puedan venir como "" (cadena vacía)
                    String[] numericFields = new String[]{"id_vendedor", "precio_producto", "id_categoria", "existencia"};
                    for (String nf : numericFields) {
                        JsonNode nnode = obj.get(nf);
                        if (nnode != null && nnode.isTextual()) {
                            String s = nnode.asText();
                            if (s == null || s.isEmpty()) {
                                obj.remove(nf);
                            } else {
                                // intentar convertir a número para evitar que Jackson falle
                                try {
                                    if (nf.equals("precio_producto")) {
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

            publicacion.setId_categoria(Integer.parseInt(ctx.formParam("id_categoria")));

            // Manejo de la imagen opcional
            UploadedFile uploadedFile = ctx.uploadedFile("foto_publicacion");
            if (uploadedFile != null) {

                // Validar tipo MIME
                String contentType = uploadedFile.contentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    ctx.status(400).result("El archivo debe ser una imagen.");
                    return;
                }

                // Leer el InputStream SIEMPRE dentro de un try-with-resources
                byte[] imagenBytes;
                try (InputStream is = uploadedFile.content()) {
                    imagenBytes = is.readAllBytes();
                }

                // Validar tamaño máximo (5MB)
                if (imagenBytes.length > 5 * 1024 * 1024) {
                    ctx.status(400).result("La imagen no debe superar los 5MB.");
                    return;
                }

                publicacion.setFoto_publicacion(imagenBytes);
            }

            publicacion.setExistencia_publicacion(Integer.parseInt(ctx.formParam("existencia")));

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