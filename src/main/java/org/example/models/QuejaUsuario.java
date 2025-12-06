package org.example.models;

import java.time.LocalDateTime;

public class QuejaUsuario {
    private int id;
    private int id_emisor;
    private int id_receptor;
    private int id_publicacion;
    private String descripcion_queja;
    private LocalDateTime fecha_emision;
    private String estado_queja;
    private String motivo_queja;
    private byte[] imagen;

    public QuejaUsuario() {
    }

    public QuejaUsuario(int id, int id_emisor, int id_receptor, int id_publicacion, String descripcion_queja, LocalDateTime fecha_emision, String estado_queja, String motivo_queja, byte[] imagen) {
        this.id = id;
        this.id_emisor = id_emisor;
        this.id_receptor = id_receptor;
        this.id_publicacion = id_publicacion;
        this.descripcion_queja = descripcion_queja;
        this.fecha_emision = fecha_emision;
        this.estado_queja = estado_queja;
        this.motivo_queja = motivo_queja;
        this.imagen = imagen;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_emisor() {
        return id_emisor;
    }

    public void setId_emisor(int id_emisor) {
        this.id_emisor = id_emisor;
    }

    public int getId_receptor() {
        return id_receptor;
    }

    public void setId_receptor(int id_receptor) {
        this.id_receptor = id_receptor;
    }

    public int getId_publicacion() {
        return id_publicacion;
    }

    public void setId_publicacion(int id_publicacion) {
        this.id_publicacion = id_publicacion;
    }

    public String getDescripcion_queja() {
        return descripcion_queja;
    }

    public void setDescripcion_queja(String descripcion_queja) {
        this.descripcion_queja = descripcion_queja;
    }

    public LocalDateTime getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(LocalDateTime fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public String getEstado_queja() {
        return estado_queja;
    }

    public void setEstado_queja(String estado_queja) {
        this.estado_queja = estado_queja;
    }

    public String getMotivo_queja() {
        return motivo_queja;
    }

    public void setMotivo_queja(String motivo_queja) {
        this.motivo_queja = motivo_queja;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}