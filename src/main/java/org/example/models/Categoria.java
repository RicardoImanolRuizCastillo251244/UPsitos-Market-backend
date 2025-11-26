package org.example.models;

public class Categoria {
    private int id_categoria;
    private String tipo;
    private String descripcion;

    public Categoria() {
    }

    public Categoria(int id_categoria, String tipo, String descripcion) {
        this.id_categoria = id_categoria;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}