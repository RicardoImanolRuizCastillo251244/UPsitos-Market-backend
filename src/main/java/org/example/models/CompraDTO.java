package org.example.models;

import java.time.LocalDateTime;

public class CompraDTO {
    private int idCompra;
    private int cantidadVendida;
    private LocalDateTime fechaVenta;
    private double precioTotal;
    private String tipoPago;
    private byte[] imagenTransferencia;
    private LocalDateTime horaEntrega;
    private LocalDateTime fechaEntrega;

    private int idPublicacion;
    private String tituloPublicacion;
    private String descripcionPublicacion;
    private byte[] fotoPublicacion;
    private double precioProducto;

    private String categoria;

    private String vendedorNombre;
    private String vendedorNumeroCuenta;
    private String vendedorTitularCuenta;

    private String compradorNombre;

    public CompraDTO() {}

    public CompraDTO(int idCompra, int cantidadVendida, LocalDateTime fechaVenta, double precioTotal, String tipoPago, byte[] imagenTransferencia, LocalDateTime horaEntrega,
                     LocalDateTime fechaEntrega, int idPublicacion, String tituloPublicacion, String descripcionPublicacion, byte[] fotoPublicacion ,
                     double precioProducto, String categoria, String vendedorNombre, String vendedorNumeroCuenta,
                     String vendedorTitularCuenta, String compradorNombre) {
        this.idCompra = idCompra;
        this.cantidadVendida = cantidadVendida;
        this.fechaVenta = fechaVenta;
        this.idPublicacion = idPublicacion;
        this.precioTotal = precioTotal;
        this.tipoPago = tipoPago;
        this.imagenTransferencia = imagenTransferencia;
        this.horaEntrega = horaEntrega;
        this.fechaEntrega = fechaEntrega;
        this.tituloPublicacion = tituloPublicacion;
        this.descripcionPublicacion = descripcionPublicacion;
        this.fotoPublicacion = fotoPublicacion;
        this.precioProducto = precioProducto;
        this.categoria = categoria;
        this.vendedorNombre = vendedorNombre;
        this.vendedorNumeroCuenta = vendedorNumeroCuenta;
        this.vendedorTitularCuenta = vendedorTitularCuenta;
        this.compradorNombre = compradorNombre;
    }

    public int getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(int cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public byte[] getImagenTransferencia() {
        return imagenTransferencia;
    }

    public void setImagenTransferencia(byte[] imagenTransferencia) {
        this.imagenTransferencia = imagenTransferencia;
    }

    public LocalDateTime getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(LocalDateTime horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getTituloPublicacion() {
        return tituloPublicacion;
    }

    public void setTituloPublicacion(String tituloPublicacion) {
        this.tituloPublicacion = tituloPublicacion;
    }

    public String getDescripcionPublicacion() {
        return descripcionPublicacion;
    }

    public void setDescripcionPublicacion(String descripcionPublicacion) {
        this.descripcionPublicacion = descripcionPublicacion;
    }

    public byte[] getFotoPublicacion() {
        return this.fotoPublicacion;
    }

    public void setFotoPublicacion(byte[] fotoPublicacion) {
        this.fotoPublicacion = fotoPublicacion;
    }

    public double getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(double precioProducto) {
        this.precioProducto = precioProducto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getVendedorNombre() {
        return vendedorNombre;
    }

    public void setVendedorNombre(String vendedorNombre) {
        this.vendedorNombre = vendedorNombre;
    }

    public String getVendedorNumeroCuenta() {
        return vendedorNumeroCuenta;
    }

    public void setVendedorNumeroCuenta(String vendedorNumeroCuenta) {
        this.vendedorNumeroCuenta = vendedorNumeroCuenta;
    }

    public String getVendedorTitularCuenta() {
        return vendedorTitularCuenta;
    }

    public void setVendedorTitularCuenta(String vendedorTitularCuenta) {
        this.vendedorTitularCuenta = vendedorTitularCuenta;
    }

    public String getCompradorNombre() {
        return compradorNombre;
    }

    public void setCompradorNombre(String compradorNombre) {
        this.compradorNombre = compradorNombre;
    }
}
