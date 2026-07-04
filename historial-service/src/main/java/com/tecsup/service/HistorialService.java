package com.tecsup.service;

import com.tecsup.client.ReservaClient;
import com.tecsup.dto.HistorialDTO;
import com.tecsup.dto.ReservaDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Historial;
import com.tecsup.repository.HistorialRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialService {

    private final HistorialRepository repository;
    private final ReservaClient reservaClient;

    public HistorialService(HistorialRepository repository,
                            ReservaClient reservaClient) {
        this.repository = repository;
        this.reservaClient = reservaClient;
    }

    public List<HistorialDTO> listar() {

        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    public HistorialDTO obtenerPorId(Long id) {

        Historial historial = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Historial no encontrado con id: " + id));

        return convertirDTO(historial);
    }

    public HistorialDTO guardar(HistorialDTO dto) {

        ReservaDTO reserva = obtenerReserva(dto.getReservaId());

        if ("No fue posible consultar el servicio. Intente nuevamente."
                .equals(reserva.getEstado())) {
            throw new RuntimeException(reserva.getEstado());
        }

        Historial historial = convertirEntidad(dto);

        return convertirDTO(repository.save(historial));
    }

    public HistorialDTO actualizar(Long id, HistorialDTO dto) {

        Historial historial = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Historial no encontrado con id: " + id));

        obtenerReserva(dto.getReservaId());

        historial.setReservaId(dto.getReservaId());
        historial.setAccion(dto.getAccion());
        historial.setFecha(dto.getFecha());

        return convertirDTO(repository.save(historial));
    }

    public void eliminar(Long id) {

        Historial historial = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Historial no encontrado con id: " + id));

        repository.delete(historial);
    }

    @CircuitBreaker(name = "reservaService", fallbackMethod = "fallbackReserva")
    @Retry(name = "reservaService")
    public ReservaDTO obtenerReserva(Long reservaId) {
        return reservaClient.obtenerReserva(reservaId);
    }

    public ReservaDTO fallbackReserva(Long reservaId, Exception ex) {

        ReservaDTO reserva = new ReservaDTO();

        reserva.setId(reservaId);
        reserva.setClienteId(null);
        reserva.setServicio("SERVICIO NO DISPONIBLE");
        reserva.setFecha(null);
        reserva.setHora(null);
        reserva.setEstado("No fue posible consultar el servicio. Intente nuevamente.");

        return reserva;
    }

    private HistorialDTO convertirDTO(Historial historial) {

        return new HistorialDTO(
                historial.getId(),
                historial.getReservaId(),
                historial.getAccion(),
                historial.getFecha()
        );
    }

    private Historial convertirEntidad(HistorialDTO dto) {

        Historial historial = new Historial();

        historial.setId(dto.getId());
        historial.setReservaId(dto.getReservaId());
        historial.setAccion(dto.getAccion());
        historial.setFecha(dto.getFecha());

        return historial;
    }

}
