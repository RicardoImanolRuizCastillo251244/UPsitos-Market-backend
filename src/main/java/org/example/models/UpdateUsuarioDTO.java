package org.example.models;

public class UpdateUsuarioDTO {
    private String nombre_usuario;
    private String correo_usuario;
    private String numero_cuenta;
    private String titular_usuario;
    private String contrasena;
    private Integer id_rol;

    // Getters y Setters
    public String getNombre_usuario() { return nombre_usuario; }
    public void setNombre_usuario(String nombre_usuario) { this.nombre_usuario = nombre_usuario; }

    public String getCorreo_usuario() { return correo_usuario; }
    public void setCorreo_usuario(String correo_usuario) { this.correo_usuario = correo_usuario; }

    public String getNumero_cuenta() { return numero_cuenta; }
    public void setNumero_cuenta(String numero_cuenta) { this.numero_cuenta = numero_cuenta; }

    public String getTitular_usuario() { return titular_usuario; }
    public void setTitular_usuario(String titular_usuario) { this.titular_usuario = titular_usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Integer getId_rol() { return id_rol; }
    public void setId_rol(Integer id_rol) { this.id_rol = id_rol; }
}