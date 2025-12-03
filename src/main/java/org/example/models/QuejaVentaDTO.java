package org.example.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class QuejaVentaDTO {
    private int id_queja;
    private int id_usuario;
    private String nombre_usuario;
    private String correo_usuario;
    private String titulo_publicacion;
    private List<String> imagenes;
    private String descripcion;
    private LocalDateTime fecha_creacion;
    private String estado;
    private String tipo_problema;
    private int id_venta;

    public QuejaVentaDTO() {
        this.imagenes = new ArrayList<>();
    }

    public QuejaVentaDTO(int id_queja, int id_usuario, String nombre_usuario, String correo_usuario, 
                        String titulo_publicacion, List<String> imagenes, String descripcion, 
                        LocalDateTime fecha_creacion, String estado, String tipo_problema, int id_venta) {
        this.id_queja = id_queja;
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.correo_usuario = correo_usuario;
        this.titulo_publicacion = titulo_publicacion;
        this.imagenes = imagenes != null ? imagenes : new ArrayList<>();
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
        this.estado = estado;
        this.tipo_problema = tipo_problema;
        this.id_venta = id_venta;
    }

    // Getters y Setters
    public int getId_queja() {
        return id_queja;
    }

    public void setId_queja(int id_queja) {
        this.id_queja = id_queja;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getCorreo_usuario() {
        return correo_usuario;
    }

    public void setCorreo_usuario(String correo_usuario) {
        this.correo_usuario = correo_usuario;
    }

    public String getTitulo_publicacion() {
        return titulo_publicacion;
    }

    public void setTitulo_publicacion(String titulo_publicacion) {
        this.titulo_publicacion = titulo_publicacion;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo_problema() {
        return tipo_problema;
    }

    public void setTipo_problema(String tipo_problema) {
        this.tipo_problema = tipo_problema;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public void addImagen(byte[] imagen) {
        if (imagen != null && imagen.length > 0) {
            String base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imagen);
            this.imagenes.add(base64Image);
        }
    }
}
