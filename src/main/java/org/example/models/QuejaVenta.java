package org.example.models;

import java.time.LocalDateTime;

public class QuejaVenta {
    private int id;
    private int id_venta;
    private int id_emisor;
    private String descripcion_queja;
    private LocalDateTime fecha_emision;
    private String estado_queja;
    private String tipo_problema;
    private byte[] imagen;

    public QuejaVenta() {
    }

    public QuejaVenta(int id, int id_venta, int id_emisor, String descripcion_queja, LocalDateTime fecha_emision, String estado_queja, String tipo_problema, byte[] imagen) {
        this.id = id;
        this.id_venta = id_venta;
        this.id_emisor = id_emisor;
        this.descripcion_queja = descripcion_queja;
        this.fecha_emision = fecha_emision;
        this.estado_queja = estado_queja;
        this.tipo_problema = tipo_problema;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public int getId_emisor() {
        return id_emisor;
    }

    public void setId_emisor(int id_emisor) {
        this.id_emisor = id_emisor;
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

    public String getTipo_problema() {
        return tipo_problema;
    }

    public void setTipo_problema(String tipo_problema) {
        this.tipo_problema = tipo_problema;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}