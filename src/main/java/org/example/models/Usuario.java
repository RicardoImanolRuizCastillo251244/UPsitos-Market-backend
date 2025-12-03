package org.example.models;

import java.time.LocalDateTime;

public class Usuario {
    private int id_usuario;
    private int id_rol;
    private String nombre_usuario;
    private String correo_usuario;
    private String contrasena;
    private boolean activo;
    private LocalDateTime creado_en;
    private LocalDateTime actualizado_en;
    private String clabe_bancaria;
    private String titular_bancario;

    public Usuario() {
    }

    public Usuario(int id_usuario, int id_rol, String nombre_usuario, String correo_usuario, byte[] contrasena, boolean activo, LocalDateTime creado_en, LocalDateTime actualizado_en,  String clabe_bancaria, String titular_bancario) {
        this.id_usuario = id_usuario;
        this.id_rol = id_rol;
        this.nombre_usuario = nombre_usuario;
        this.correo_usuario = correo_usuario;
        this.contrasena = contrasena;
        this.activo = activo;
        this.creado_en = creado_en;
        this.actualizado_en = actualizado_en;
        this.clabe_bancaria = clabe_bancaria;
        this.titular_bancario = titular_bancario;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getCreado_en() {
        return creado_en;
    }

    public void setCreado_en(LocalDateTime creado_en) {
        this.creado_en = creado_en;
    }

    public LocalDateTime getActualizado_en() {
        return actualizado_en;
    }

    public void setActualizado_en(LocalDateTime actualizado_en) {
        this.actualizado_en = actualizado_en;
    }

    public String getClabe_bancaria() {
        return clabe_bancaria;
    }

    public void setClabe_bancaria(String clabe_bancaria) {
        this.clabe_bancaria = clabe_bancaria;
    }

    public String getTitular_bancario() {
        return titular_bancario;
    }

    public void setTitular_bancario(String titular_bancario) {
        this.titular_bancario = titular_bancario;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id_usuario=" + id_usuario +
                ", id_rol=" + id_rol +
                ", nombre_usuario='" + nombre_usuario + '\'' +
                ", correo_usuario='" + correo_usuario + '\'' +
                ", contrasena=[PROTEGIDO]" +
                ", activo=" + activo +
                ", creado_en=" + creado_en +
                ", actualizado_en=" + actualizado_en +
                '}';
    }
}