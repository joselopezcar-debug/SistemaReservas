package com.tecsup.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cliente-service")
public interface ClienteClient {
    @GetMapping("/clientes/{id}")
    ClienteDTO obtenerCliente(@PathVariable Long id);
}
