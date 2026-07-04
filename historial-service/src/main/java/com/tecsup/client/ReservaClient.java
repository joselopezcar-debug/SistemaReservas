package com.tecsup.client;

import com.tecsup.dto.ReservaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "reserva-service",
        fallback = ReservaClientFallback.class
)
public interface ReservaClient {
    @GetMapping("/api/reservas/{id}")
    ReservaDTO obtenerReserva(@PathVariable Long id);
}
