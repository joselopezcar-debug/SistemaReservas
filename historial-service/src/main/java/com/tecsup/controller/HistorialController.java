package com.tecsup.controller;

import com.tecsup.dto.HistorialDTO;
import com.tecsup.service.HistorialService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialController {

    private final HistorialService service;

    public HistorialController(HistorialService service) {
        this.service = service;
    }

    @GetMapping
    public List<HistorialDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public HistorialDTO obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HistorialDTO guardar(@RequestBody HistorialDTO dto) {
        return service.guardar(dto);
    }

    @PutMapping("/{id}")
    public HistorialDTO actualizar(@PathVariable Long id,
                                   @RequestBody HistorialDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

}
