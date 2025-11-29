package org.example.models;

import java.time.LocalDateTime;

public class Publicacion {
    private Integer id_publicacion;
    private String titulo_publicacion;
    private String descripcion_publicacion;
    private byte[] foto_publicacion;
    private LocalDateTime fecha_publicacion;
    private LocalDateTime fecha_expiracion;
    private String estado_publicacion;
    private Integer id_vendedor;
    private Float precio_producto;
    private Integer id_categoria;
    private Integer existencia;

    public Publicacion() {
    }
    public Publicacion(Integer id_publicacion, String titulo_publicacion, String descripcion_publicacion, byte[] foto_publicacion, LocalDateTime fecha_publicacion, LocalDateTime fecha_expiracion, String estado_publicacion, Integer id_vendedor, Float precio_producto, Integer id_categoria) {
        this.id_publicacion = id_publicacion;
        this.titulo_publicacion = titulo_publicacion;
        this.descripcion_publicacion = descripcion_publicacion;
        this.foto_publicacion = foto_publicacion;
        this.fecha_publicacion = fecha_publicacion;
        this.fecha_expiracion = fecha_expiracion;
        this.estado_publicacion = estado_publicacion;
        this.id_vendedor = id_vendedor;
        this.precio_producto = precio_producto;
        this.id_categoria = id_categoria;
    }

    public Integer getId_publicacion() {
        return id_publicacion;
    }

    public void setId_publicacion(Integer id_publicacion) {
        this.id_publicacion = id_publicacion;
    }

    public String getTitulo_publicacion() {
        return titulo_publicacion;
    }

    public void setTitulo_publicacion(String titulo_publicacion) {
        this.titulo_publicacion = titulo_publicacion;
    }

    public String getDescripcion_publicacion() {
        return descripcion_publicacion;
    }

    public void setDescripcion_publicacion(String descripcion_publicacion) {
        this.descripcion_publicacion = descripcion_publicacion;
    }

    public byte[] getFoto_publicacion() {
        return foto_publicacion;
    }

    public void setFoto_publicacion(byte[] foto_publicacion) {
        this.foto_publicacion = foto_publicacion;
    }

    public LocalDateTime getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(LocalDateTime fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public LocalDateTime getFecha_expiracion() {
        return fecha_expiracion;
    }

    public void setFecha_expiracion(LocalDateTime fecha_expiracion) {
        this.fecha_expiracion = fecha_expiracion;
    }

    public String getEstado_publicacion() {
        return estado_publicacion;
    }

    public void setEstado_publicacion(String estado_publicacion) {
        this.estado_publicacion = estado_publicacion;
    }

    public Integer getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(Integer id_vendedor) {
        this.id_vendedor = id_vendedor;
    }

    public Float getPrecio_producto() {
        return precio_producto;
    }

    public void setPrecio_producto(Float precio_producto) {
        this.precio_producto = precio_producto;
    }

    public Integer getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(Integer id_categoria) {
        this.id_categoria = id_categoria;
    }

    public Integer getExistencia() {
        return existencia;
    }

    public void setExistencia(Integer existencia) {
        this.existencia = existencia;
    }
}