package org.example.models;

import java.time.LocalDateTime;

public class UsuarioMembresia {
    private int id_usuario_membresia;
    private int id_usuario;
    private int id_membresia_tipo;
    private LocalDateTime fecha_inicio;
    private LocalDateTime fecha_expiracion;
    private boolean activa;

    public UsuarioMembresia() {
    }

    public UsuarioMembresia(int id_usuario_membresia, int id_usuario, int id_membresia_tipo, LocalDateTime fecha_inicio, LocalDateTime fecha_expiracion, boolean activa) {
        this.id_usuario_membresia = id_usuario_membresia;
        this.id_usuario = id_usuario;
        this.id_membresia_tipo = id_membresia_tipo;
        this.fecha_inicio = fecha_inicio;
        this.fecha_expiracion = fecha_expiracion;
        this.activa = activa;
    }

    public int getId_usuario_membresia() {
        return id_usuario_membresia;
    }

    public void setId_usuario_membresia(int id_usuario_membresia) {
        this.id_usuario_membresia = id_usuario_membresia;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_membresia_tipo() {
        return id_membresia_tipo;
    }

    public void setId_membresia_tipo(int id_membresia_tipo) {
        this.id_membresia_tipo = id_membresia_tipo;
    }

    public LocalDateTime getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDateTime fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalDateTime getFecha_expiracion() {
        return fecha_expiracion;
    }

    public void setFecha_expiracion(LocalDateTime fecha_expiracion) {
        this.fecha_expiracion = fecha_expiracion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}