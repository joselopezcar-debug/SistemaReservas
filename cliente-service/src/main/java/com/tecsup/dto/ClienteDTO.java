package com.tecsup.dto;

import lombok.Data;

@Data
public class ClienteDTO {

    private Long id;

    private String nombre;

    private String apellido;

    private String correo;

    private String telefono;

    public ClienteDTO() {
    }

    public ClienteDTO(Long id, String nombre, String apellido, String correo, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
    }

}
