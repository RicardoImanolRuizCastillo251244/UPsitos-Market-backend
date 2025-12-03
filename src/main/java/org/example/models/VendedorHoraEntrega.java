package org.example.models;

import java.time.LocalTime;

public class VendedorHoraEntrega {
    private int idVendedorHora;
    private int idVendedor;
    private LocalTime horaEntrega;

    public VendedorHoraEntrega() {
    }

    public VendedorHoraEntrega(int idVendedorHora, int idVendedor, LocalTime horaEntrega) {
        this.idVendedorHora = idVendedorHora;
        this.idVendedor = idVendedor;
        this.horaEntrega = horaEntrega;
    }

    public int getIdVendedorHora() {
        return idVendedorHora;
    }

    public void setIdVendedorHora(int idVendedorHora) {
        this.idVendedorHora = idVendedorHora;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public LocalTime getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(LocalTime horaEntrega) {
        this.horaEntrega = horaEntrega;
    }
}
