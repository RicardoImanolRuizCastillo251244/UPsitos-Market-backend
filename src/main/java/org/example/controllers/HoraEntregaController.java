package org.example.controllers;

import io.javalin.http.Context;
import org.example.models.HorarioDTO;
import org.example.services.HoraEntregaService;

public class HoraEntregaController {

    private final HoraEntregaService horaEntregaService;

    public HoraEntregaController(HoraEntregaService horaEntregaService) {
        this.horaEntregaService = horaEntregaService;
    }

    // Recibir varios horarios
    public void save(Context ctx) {
        try {
            HorarioDTO dto = ctx.bodyAsClass(HorarioDTO.class);

            horaEntregaService.saveMultiple(dto.id_usuario, dto.horarios);

            ctx.status(201).result("Horarios guardados correctamente");
        } catch (Exception e) {
            ctx.status(500).result("Error al guardar horarios: " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id =  Integer.parseInt(ctx.pathParam("id"));
            horaEntregaService.delete(id);
            ctx.status(204).result("Horarios eliminados correctamente");
        }
        catch (Exception e) {
            ctx.status(500).result("Error al eliminar horarios: " + e.getMessage());
        }
    }

    public void findAll(Context ctx){
        try {
            ctx.json(horaEntregaService.findAll());
        }
        catch (Exception e) {
            ctx.status(500).result("Error al obtener horarios: " + e.getMessage());
        }
    }

    // Obtener horarios de un usuario
    public void findById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(horaEntregaService.findById(id));
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener horarios: " + e.getMessage());
        }
    }
}
