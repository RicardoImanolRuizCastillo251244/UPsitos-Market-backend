package org.example.controllers;

import io.javalin.http.Context;
import org.example.models.Rol;
import org.example.services.RolService;

public class RolController {
    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    public void saveRol(Context ctx) {
        try {
            Rol rol = ctx.bodyAsClass(Rol.class);
            Rol savedRol = rolService.save(rol);
            ctx.status(201).json(savedRol);
        } catch (Exception e) {
            ctx.status(400).result("Error al guardar el rol: " + e.getMessage());
        }
    }

    public void updateRol(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Rol rol = ctx.bodyAsClass(Rol.class);
            rol.setId_rol(id);
            rolService.update(rol);
            ctx.status(204);
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID de rol inválido.");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar el rol: " + e.getMessage());
        }
    }

    public void deleteRol(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            rolService.deleteById(id);
            ctx.status(204);
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID de rol inválido.");
        } catch (Exception e) {
            ctx.status(400).result("Error al eliminar el rol: " + e.getMessage());
        }
    }

    public void getRolById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            rolService.findById(id)
                    .ifPresentOrElse(
                            rol -> ctx.status(200).json(rol),
                            () -> ctx.status(404).result("Rol no encontrado.")
                    );
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID de rol inválido.");
        } catch (Exception e) {
            ctx.status(500).result("Error al buscar el rol: " + e.getMessage());
        }
    }

    public void getAllRoles(Context ctx) {
        try {
            ctx.status(200).json(rolService.findAll());
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener los roles: " + e.getMessage());
        }
    }
}