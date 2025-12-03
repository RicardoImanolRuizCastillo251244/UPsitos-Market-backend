package org.example.models;


public class HoraEntrega {
    private int id_horario;
    private int id_usuario;
    private int hora;

    public HoraEntrega(){}

    public HoraEntrega(int id_horario, int hora) {
        this.id_horario = id_horario;
        this.hora = hora;
    }

    public HoraEntrega(int id_horario, int hora, int id_usuario) {
        this.id_horario = id_horario;
        this.hora = hora;
        this.id_usuario = id_usuario;
    }

    public int getId_horario() {
        return id_horario;
    }

    public void setId_horario(int id_horario) {
        this.id_horario = id_horario;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }
}
