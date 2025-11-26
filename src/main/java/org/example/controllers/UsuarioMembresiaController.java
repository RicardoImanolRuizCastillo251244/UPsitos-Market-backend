package org.example.controllers;

import io.javalin.http.Context;
import org.example.models.UsuarioMembresia;
import org.example.services.UsuarioMembresiaService;

public class UsuarioMembresiaController {
    private final UsuarioMembresiaService umService;

    public UsuarioMembresiaController(UsuarioMembresiaService umService) {
        this.umService = umService;
    }

    public void saveUsuarioMembresia(Context ctx) {
        try {
            UsuarioMembresia um = ctx.bodyAsClass(UsuarioMembresia.class);
            UsuarioMembresia savedUm = umService.save(um);
            ctx.status(201).json(savedUm);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al guardar la membresía de usuario: " + e.getMessage());
        }
    }

    public void updateUsuarioMembresia(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            UsuarioMembresia um = ctx.bodyAsClass(UsuarioMembresia.class);
            um.setId_usuario_membresia(id);
            umService.update(um);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al actualizar la membresía de usuario: " + e.getMessage());
        }
    }

    public void deleteUsuarioMembresia(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            umService.deleteById(id);
            ctx.status(204);
        } catch (Exception e) {
            ctx.status(500).result("Error al eliminar la membresía de usuario: " + e.getMessage());
        }
    }

    public void getUsuarioMembresiaById(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            umService.findById(id)
                    .ifPresentOrElse(
                            um -> ctx.status(200).json(um),
                            () -> ctx.status(404).result("Membresía de usuario no encontrada.")
                    );
        } catch (Exception e) {
            ctx.status(500).result("Error al buscar la membresía de usuario: " + e.getMessage());
        }
    }

    public void getAllUsuarioMembresias(Context ctx) {
        try {
            ctx.status(200).json(umService.findAll());
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener las membresías de usuario: " + e.getMessage());
        }
    }
}