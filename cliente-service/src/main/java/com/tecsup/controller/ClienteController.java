package com.tecsup.controller;

import com.tecsup.dto.ClienteDTO;
import com.tecsup.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<ClienteDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ClienteDTO obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO guardar(@RequestBody ClienteDTO dto) {
        return service.guardar(dto);
    }

    @PutMapping("/{id}")
    public ClienteDTO actualizar(@PathVariable Long id,
                                 @RequestBody ClienteDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

}
