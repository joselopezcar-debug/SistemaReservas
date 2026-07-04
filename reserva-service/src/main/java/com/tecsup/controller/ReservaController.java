package com.tecsup.controller;

import com.tecsup.dto.ReservaDTO;
import com.tecsup.service.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReservaDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ReservaDTO obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaDTO guardar(@RequestBody ReservaDTO dto) {
        return service.guardar(dto);
    }

    @PutMapping("/{id}")
    public ReservaDTO actualizar(@PathVariable Long id,
                                 @RequestBody ReservaDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

}
