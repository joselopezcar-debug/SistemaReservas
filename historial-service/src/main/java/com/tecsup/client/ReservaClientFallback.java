package com.tecsup.client;

import com.tecsup.dto.ReservaDTO;
import org.springframework.stereotype.Component;

@Component
public class ReservaClientFallback implements ReservaClient {

    @Override
    public ReservaDTO obtenerReserva(Long id) {
        ReservaDTO dto = new ReservaDTO();
        dto.setEstado("SERVICIO_NO_DISPONIBLE");
        return dto;
    }
}
