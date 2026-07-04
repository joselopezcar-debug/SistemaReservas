package com.tecsup.dto;

import java.time.LocalDate;

public class ReservaDTO {

    private Long id;
    private Long clienteId;
    private String servicio;
    private LocalDate fecha;
    private String hora;
    private String estado;

    public ReservaDTO() {
    }

    public ReservaDTO(Long id, Long clienteId, String servicio,
                      LocalDate fecha, String hora, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.servicio = servicio;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
