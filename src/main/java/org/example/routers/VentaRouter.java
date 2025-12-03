package org.example.routers;

import io.javalin.Javalin;
import org.example.controllers.VentaController;

public class VentaRouter {
    private final VentaController ventaController;

    public VentaRouter(VentaController ventaController) {
        this.ventaController = ventaController;
    }

    public void register(Javalin app) {
        String basePath = "/venta";
        app.post(basePath, ventaController::saveVenta);
        app.get(basePath, ventaController::getAllVentas);
        app.get(basePath + "/{id}", ventaController::getVentaById);
        app.put(basePath + "/{id}", ventaController::updateVenta);
        app.delete(basePath + "/{id}", ventaController::deleteVenta);
        app.get(basePath + "/publicaciones-mas-vendidas", ventaController::getPublicacionesMasVendidas);
        app.get("/compras/{id}",ventaController::getAllCompras);
        app.get("/venta/vendedor/{id}", ventaController::getVentasByVendedor);
    }
}