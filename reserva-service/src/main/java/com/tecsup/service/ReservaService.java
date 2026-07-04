package com.tecsup.service;

import com.tecsup.client.ClienteClient;
import com.tecsup.dto.ClienteDTO;
import com.tecsup.dto.ReservaDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Reserva;
import com.tecsup.repository.ReservaRepository;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository repository;
    private final ClienteClient clienteClient;
    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    public ReservaService(ReservaRepository repository,
                          ClienteClient clienteClient) {
        this.repository = repository;
        this.clienteClient = clienteClient;
    }

    public List<ReservaDTO> listar() {

        logger.info("Listando reservas");

        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    public ReservaDTO obtenerPorId(Long id) {

        logger.info("Buscando reserva con id {}", id);

        Reserva reserva = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        return convertirDTO(reserva);
    }

    public ReservaDTO guardar(ReservaDTO dto) {

        logger.info("Verificando existencia del cliente con id {}", dto.getClienteId());

        ClienteDTO cliente = clienteClient.obtenerCliente(dto.getClienteId());

        if (cliente == null) {
            logger.error("Cliente {} no encontrado", dto.getClienteId());
            throw new ResourceNotFoundException("Cliente no encontrado");
        }

        logger.info("Cliente encontrado correctamente");

        Reserva reserva = convertirEntidad(dto);

        logger.info("Registrando nueva reserva");

        Reserva guardada = repository.save(reserva);

        logger.info("Reserva registrada correctamente con id {}", guardada.getId());

        return convertirDTO(guardada);
    }

    public ReservaDTO actualizar(Long id, ReservaDTO dto) {

        logger.info("Actualizando reserva con id {}", id);

        Reserva reserva = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        logger.info("Verificando existencia del cliente con id {}", dto.getClienteId());

        clienteClient.obtenerCliente(dto.getClienteId());

        logger.info("Cliente encontrado correctamente");

        reserva.setClienteId(dto.getClienteId());
        reserva.setServicio(dto.getServicio());
        reserva.setFecha(dto.getFecha());
        reserva.setHora(dto.getHora());
        reserva.setEstado(dto.getEstado());

        Reserva actualizada = repository.save(reserva);

        logger.info("Reserva {} actualizada correctamente", id);

        return convertirDTO(actualizada);
    }

    public void eliminar(Long id) {

        logger.info("Eliminando reserva con id {}", id);

        Reserva reserva = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        repository.delete(reserva);

        logger.info("Reserva {} eliminada correctamente", id);
    }

    private ReservaDTO convertirDTO(Reserva reserva) {

        return new ReservaDTO(
                reserva.getId(),
                reserva.getClienteId(),
                reserva.getServicio(),
                reserva.getFecha(),
                reserva.getHora(),
                reserva.getEstado()
        );
    }

    private Reserva convertirEntidad(ReservaDTO dto) {

        Reserva reserva = new Reserva();

        reserva.setId(dto.getId());
        reserva.setClienteId(dto.getClienteId());
        reserva.setServicio(dto.getServicio());
        reserva.setFecha(dto.getFecha());
        reserva.setHora(dto.getHora());
        reserva.setEstado(dto.getEstado());

        return reserva;
    }
}
