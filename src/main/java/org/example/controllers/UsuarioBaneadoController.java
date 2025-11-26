package org.example.controllers;

import io.javalin.http.Context;
import org.example.models.UsuarioBaneado;
import org.example.services.UsuarioBaneadoService;

public class UsuarioBaneadoController {
    private final UsuarioBaneadoService ubService;

    public UsuarioBaneadoController(UsuarioBaneadoService ubService) {
        this.ubService = ubService;
    }

    private int parseId(String idStr) {
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El ID debe ser un número válido.");
        }
    }

    public void banearUsuario(Context ctx) {
        try {
            UsuarioBaneado ub = ctx.bodyAsClass(UsuarioBaneado.class);
            UsuarioBaneado savedUb = ubService.banearUsuario(ub);
            ctx.status(201).json(savedUb);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al banear al usuario: " + e.getMessage());
        }
    }

    public void desbanearUsuario(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id_usuario"));
            ubService.desbanearUsuario(id);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al desbanear al usuario: " + e.getMessage());
        }
    }

    public void getBaneadoById(Context ctx) {
        try {
            int id = parseId(ctx.pathParam("id_usuario"));
            ubService.findById(id)
                    .ifPresentOrElse(
                            ub -> ctx.status(200).json(ub),
                            () -> ctx.status(404).result("El usuario no se encuentra en la lista de baneados.")
                    );
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al buscar al usuario baneado: " + e.getMessage());
        }
    }

    public void getAllBaneados(Context ctx) {
        try {
            ctx.status(200).json(ubService.findAll());
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener la lista de usuarios baneados: " + e.getMessage());
        }
    }
}