package org.example.controllers;

import io.javalin.http.Context;
import org.example.models.MembresiaTipo;
import org.example.services.MembresiaTipoService;

public class MembresiaTipoController {
    private final MembresiaTipoService membresiaTipoService;

    public MembresiaTipoController(MembresiaTipoService membresiaTipoService) {
        this.membresiaTipoService = membresiaTipoService;
    }

    public void saveMembresiaTipo(Context ctx) {
        try {
            MembresiaTipo membresiaTipo = ctx.bodyAsClass(MembresiaTipo.class);
            MembresiaTipo savedMembresiaTipo = membresiaTipoService.save(membresiaTipo);
            ctx.status(201).json(savedMembresiaTipo);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al guardar el tipo de membresía: " + e.getMessage());
        }
    }

    public void updateMembresiaTipo(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            MembresiaTipo membresiaTipo = ctx.bodyAsClass(MembresiaTipo.class);
            membresiaTipo.setId_membresia_tipo(id);
            membresiaTipoService.update(membresiaTipo);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al actualizar el tipo de membresía: " + e.getMessage());
        }
    }

    public void deleteMembresiaTipo(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            membresiaTipoService.deleteById(id);
            ctx.status(204);
        } catch (Exception e) {
            ctx.status(500).result("Error al eliminar el tipo de membresía: " + e.getMessage());
        }
    }

    public void getMembresiaTipoById(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            membresiaTipoService.findById(id)
                    .ifPresentOrElse(
                            membresiaTipo -> ctx.status(200).json(membresiaTipo),
                            () -> ctx.status(404).result("Tipo de membresía no encontrado.")
                    );
        } catch (Exception e) {
            ctx.status(500).result("Error al buscar el tipo de membresía: " + e.getMessage());
        }
    }

    public void getAllMembresiaTipos(Context ctx) {
        try {
            ctx.status(200).json(membresiaTipoService.findAll());
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener los tipos de membresía: " + e.getMessage());
        }
    }
}