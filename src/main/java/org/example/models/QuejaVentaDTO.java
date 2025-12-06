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
    
    // Información adicional para el administrador
    private String tipo_pago;
    private String imagen_comprobante_pago; // Base64 de la imagen de VENTA
    private String imagen_evidencia_queja;  // Base64 de la imagen de QUEJA_VENTA
    private String nombre_comprador;
    private String correo_comprador;
    private int id_comprador;
    private String nombre_vendedor;
    private String correo_vendedor;
    private int id_vendedor;

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

    // Getters y Setters para campos adicionales
    public String getTipo_pago() {
        return tipo_pago;
    }

    public void setTipo_pago(String tipo_pago) {
        this.tipo_pago = tipo_pago;
    }

    public String getImagen_comprobante_pago() {
        return imagen_comprobante_pago;
    }

    public void setImagen_comprobante_pago(String imagen_comprobante_pago) {
        this.imagen_comprobante_pago = imagen_comprobante_pago;
    }

    public String getImagen_evidencia_queja() {
        return imagen_evidencia_queja;
    }

    public void setImagen_evidencia_queja(String imagen_evidencia_queja) {
        this.imagen_evidencia_queja = imagen_evidencia_queja;
    }

    public String getNombre_comprador() {
        return nombre_comprador;
    }

    public void setNombre_comprador(String nombre_comprador) {
        this.nombre_comprador = nombre_comprador;
    }

    public String getCorreo_comprador() {
        return correo_comprador;
    }

    public void setCorreo_comprador(String correo_comprador) {
        this.correo_comprador = correo_comprador;
    }

    public int getId_comprador() {
        return id_comprador;
    }

    public void setId_comprador(int id_comprador) {
        this.id_comprador = id_comprador;
    }

    public String getNombre_vendedor() {
        return nombre_vendedor;
    }

    public void setNombre_vendedor(String nombre_vendedor) {
        this.nombre_vendedor = nombre_vendedor;
    }

    public String getCorreo_vendedor() {
        return correo_vendedor;
    }

    public void setCorreo_vendedor(String correo_vendedor) {
        this.correo_vendedor = correo_vendedor;
    }

    public int getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(int id_vendedor) {
        this.id_vendedor = id_vendedor;
    }
}
