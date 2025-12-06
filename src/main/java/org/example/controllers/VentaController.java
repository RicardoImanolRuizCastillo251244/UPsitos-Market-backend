package org.example.controllers;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.models.Venta;
import org.example.services.VentaService;

import java.util.Optional;

public class VentaController {
    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    public void saveVenta(Context ctx) {
        try {
            // Crear objeto Venta manualmente desde form params
            Venta venta = new Venta();
            venta.setId_publicacion(Integer.parseInt(ctx.formParam("id_publicacion")));
            venta.setCantidad_vendida(Integer.parseInt(ctx.formParam("cantidad_vendida")));
            venta.setPrecio_total(Float.parseFloat(ctx.formParam("precio_total")));
            venta.setId_comprador(Integer.parseInt(ctx.formParam("id_comprador")));

            // Manejo del tipo de pago
            String tipoPago = ctx.formParam("tipo_pago");
            if (tipoPago != null && !tipoPago.isEmpty()) {
                venta.setTipo_pago(tipoPago);
            } else {
                venta.setTipo_pago("Desconocido"); // O lanza un error si es obligatorio
            }

            // Manejo de la imagen opcional
            UploadedFile uploadedFile = ctx.uploadedFile("imagen");
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
                venta.setImagen(imagenBytes);
            }

            Venta savedVenta = ventaService.save(venta);
            ctx.status(201).json(savedVenta);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al procesar la venta: " + e.getMessage());
        }
    }

    public void getPublicacionesMasVendidas(Context ctx) {
        try {
            ctx.json(ventaService.getPublicacionesMasVendidas());
            ctx.status(200);
        } catch (Exception e) {
            ctx.status(404).result(e.getMessage());
        }
    }


    public void updateVenta(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            Venta venta = ctx.bodyAsClass(Venta.class);
            venta.setId(id);
            ventaService.update(venta);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("Datos de venta o ID inválidos.");
        } catch (Exception e) {
            ctx.status(500).result("Error al actualizar la venta: " + e.getMessage());
        }
    }

    public void deleteVenta(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            ventaService.deleteById(id);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("ID de venta inválido.");
        } catch (Exception e) {
            ctx.status(500).result("Error al eliminar la venta: " + e.getMessage());
        }
    }

    public void getVentaById(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            Venta venta = ventaService.findById(id).orElse(null);
            if(venta == null){
                ctx.status(404).result("El venta no existe");
            }
            else {
                ctx.status(200).json(venta);
            }
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("ID de venta inválido.");
        } catch (Exception e) {
            ctx.status(500).result("Error al buscar la venta: " + e.getMessage());
        }
    }

    public void getAllVentas(Context ctx) {
        try {
            ctx.status(200).json(ventaService.findAll());
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener las ventas: " + e.getMessage());
        }
    }

    public void getAllCompras(Context ctx) {
        try {
            int id =  ctx.pathParamAsClass("id", Integer.class).get();
            ctx.status(200).json(ventaService.findAllCompras(id));
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener las compras: " + e.getMessage());
        }
    }

    public void getVentasByVendedor(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            ctx.status(200).json(ventaService.findVentasByVendedor(id));
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener las ventas del vendedor: " + e.getMessage());
        }
    }
}