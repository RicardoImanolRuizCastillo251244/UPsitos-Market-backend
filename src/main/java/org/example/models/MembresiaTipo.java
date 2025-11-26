package org.example.models;

public class MembresiaTipo {
    private int id_membresia_tipo;
    private float precio;
    private String descripcion;

    public MembresiaTipo() {
    }

    public MembresiaTipo(int id_membresia_tipo, float precio, String descripcion) {
        this.id_membresia_tipo = id_membresia_tipo;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public int getId_membresia_tipo() {
        return id_membresia_tipo;
    }

    public void setId_membresia_tipo(int id_membresia_tipo) {
        this.id_membresia_tipo = id_membresia_tipo;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}