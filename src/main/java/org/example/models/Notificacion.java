package org.example.models;

import java.time.LocalDateTime;

public class Notificacion {
    private int id;
    private int id_usuario;
    private String mensaje;
    private LocalDateTime fecha_envio;
    private boolean leida;
    private String tipo;

    public Notificacion() {
    }

    public Notificacion(int id, int id_usuario, String mensaje, LocalDateTime fecha_envio, boolean leida, String tipo) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.mensaje = mensaje;
        this.fecha_envio = fecha_envio;
        this.leida = leida;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha_envio() {
        return fecha_envio;
    }

    public void setFecha_envio(LocalDateTime fecha_envio) {
        this.fecha_envio = fecha_envio;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}