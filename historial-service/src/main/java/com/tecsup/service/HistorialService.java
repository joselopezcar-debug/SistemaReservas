package com.tecsup.service;

import com.tecsup.client.ReservaClient;
import com.tecsup.dto.*;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Historial;
import com.tecsup.repository.HistorialRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialService {

    private final HistorialRepository repository;
    private final ReservaClient reservaClient;
    private static final Logger logger = LoggerFactory.getLogger(HistorialService.class);

    public HistorialService(HistorialRepository repository,
                            ReservaClient reservaClient) {
        this.repository = repository;
        this.reservaClient = reservaClient;
    }

    public List<HistorialDTO> listar() {

        logger.info("Listando historial");

        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    public HistorialDTO obtenerPorId(Long id) {

        logger.info("Buscando historial con id {}", id);

        Historial historial = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Historial no encontrado con id: " + id));

        return convertirDTO(historial);
    }

    @CircuitBreaker(name = "reservaService", fallbackMethod = "fallbackReserva")
    public HistorialDTO guardar(HistorialDTO dto) {

        logger.info("Consultando reserva {} desde reserva-service", dto.getReservaId());

        ReservaDTO reserva = reservaClient.obtenerReserva(dto.getReservaId());

        logger.info("Reserva encontrada correctamente");

        Historial historial = convertirEntidad(dto);

        logger.info("Registrando nuevo historial");

        Historial guardado = repository.save(historial);

        logger.info("Historial registrado correctamente con id {}", guardado.getId());

        return convertirDTO(guardado);
    }

    public HistorialDTO actualizar(Long id, HistorialDTO dto) {

        logger.info("Actualizando historial con id {}", id);

        Historial historial = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Historial no encontrado con id: " + id));

        logger.info("Consultando reserva {} desde reserva-service", dto.getReservaId());

        obtenerReserva(dto.getReservaId());

        logger.info("Reserva encontrada correctamente");

        historial.setReservaId(dto.getReservaId());
        historial.setAccion(dto.getAccion());
        historial.setFecha(dto.getFecha());

        Historial actualizado = repository.save(historial);

        logger.info("Historial {} actualizado correctamente", id);

        return convertirDTO(actualizado);
    }

    private HistorialDTO fallbackReserva(HistorialDTO dto, Throwable ex) {

        logger.error("Circuit Breaker activado. reserva-service no disponible: {}", ex.getMessage());

        HistorialDTO fallback = new HistorialDTO();
        fallback.setReservaId(dto.getReservaId());
        fallback.setAccion("No fue posible consultar el servicio. Intente nuevamente.");

        return fallback;
    }

    public void eliminar(Long id) {

        logger.info("Eliminando historial con id {}", id);

        Historial historial = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Historial no encontrado con id: " + id));

        repository.delete(historial);

        logger.info("Historial {} eliminado correctamente", id);
    }

    @CircuitBreaker(name = "reservaService", fallbackMethod = "fallbackReserva")
    @Retry(name = "reservaService")
    public ReservaDTO obtenerReserva(Long reservaId) {

        logger.info("Consultando reserva {} desde reserva-service", reservaId);

        return reservaClient.obtenerReserva(reservaId);
    }

    public ReservaDTO fallbackReserva(Long reservaId, Exception ex) {

        logger.error("No se pudo consultar la reserva {}: {}", reservaId, ex.getMessage());

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
