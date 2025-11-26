package org.example.models;

import java.time.LocalDateTime;

public class UsuarioBaneado {
    private int id_usuario;
    private LocalDateTime fecha;
    private String motivo;

    public UsuarioBaneado() {
    }

    public UsuarioBaneado(int id_usuario, LocalDateTime fecha, String motivo) {
        this.id_usuario = id_usuario;
        this.fecha = fecha;
        this.motivo = motivo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}