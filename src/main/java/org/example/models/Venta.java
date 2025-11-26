package org.example.models;

import java.time.LocalDateTime;

public class Venta {
    private int id;
    private int id_publicacion;
    private int cantidad_vendida;
    private LocalDateTime fecha_venta;
    private float precio_total;
    private int id_comprador;
    private byte[] imagen;

    public Venta() {
    }

    public Venta(int id, int id_publicacion, int cantidad_vendida, LocalDateTime fecha_venta, float precio_total, int id_comprador, byte[] imagen) {
        this.id = id;
        this.id_publicacion = id_publicacion;
        this.cantidad_vendida = cantidad_vendida;
        this.fecha_venta = fecha_venta;
        this.precio_total = precio_total;
        this.id_comprador = id_comprador;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_publicacion() {
        return id_publicacion;
    }

    public void setId_publicacion(int id_publicacion) {
        this.id_publicacion = id_publicacion;
    }

    public int getCantidad_vendida() {
        return cantidad_vendida;
    }

    public void setCantidad_vendida(int cantidad_vendida) {
        this.cantidad_vendida = cantidad_vendida;
    }

    public LocalDateTime getFecha_venta() {
        return fecha_venta;
    }

    public void setFecha_venta(LocalDateTime fecha_venta) {
        this.fecha_venta = fecha_venta;
    }

    public float getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(float precio_total) {
        this.precio_total = precio_total;
    }

    public int getId_comprador() {
        return id_comprador;
    }

    public void setId_comprador(int id_comprador) {
        this.id_comprador = id_comprador;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}