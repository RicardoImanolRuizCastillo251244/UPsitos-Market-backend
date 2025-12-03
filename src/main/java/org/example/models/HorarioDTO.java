package org.example.models;
public class HorarioDTO {
    public int id_usuario;
    public int[] horarios;

    public HorarioDTO(){

    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int[] getHorarios() {
        return horarios;
    }

    public void setHorarios(int[] horarios) {
        this.horarios = horarios;
    }
}

