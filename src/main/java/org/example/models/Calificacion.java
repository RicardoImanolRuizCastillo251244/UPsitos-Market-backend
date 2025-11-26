package org.example.models;

public class Calificacion {
    private int id_calificacion;
    private int id_usuario_califica;
    private int id_publicacion;
    private int calificacion;
    private String experiencia;

    public Calificacion() {
    }

    public Calificacion(int id_calificacion, int id_usuario_califica, int id_publicacion, int calificacion, String experiencia) {
        this.id_calificacion = id_calificacion;
        this.id_usuario_califica = id_usuario_califica;
        this.id_publicacion = id_publicacion;
        this.calificacion = calificacion;
        this.experiencia = experiencia;
    }

    public int getId_calificacion() {
        return id_calificacion;
    }

    public void setId_calificacion(int id_calificacion) {
        this.id_calificacion = id_calificacion;
    }

    public int getId_usuario_califica() {
        return id_usuario_califica;
    }

    public void setId_usuario_califica(int id_usuario_califica) {
        this.id_usuario_califica = id_usuario_califica;
    }

    public int getId_publicacion() {
        return id_publicacion;
    }

    public void setId_publicacion(int id_publicacion) {
        this.id_publicacion = id_publicacion;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }
}