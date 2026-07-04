package com.tecsup.dto;

import java.time.LocalDateTime;

public class HistorialDTO {

    private Long id;
    private Long reservaId;
    private String accion;
    private LocalDateTime fecha;

    public HistorialDTO() {
    }

    public HistorialDTO(Long id, Long reservaId, String accion, LocalDateTime fecha) {
        this.id = id;
        this.reservaId = reservaId;
        this.accion = accion;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public String getAccion() {
        return accion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
