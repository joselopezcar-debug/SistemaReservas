package com.tecsup.service;

import com.tecsup.client.ClienteClient;
import com.tecsup.dto.ClienteDTO;
import com.tecsup.dto.ReservaDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Reserva;
import com.tecsup.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository repository;
    private final ClienteClient clienteClient;

    public ReservaService(ReservaRepository repository,
                          ClienteClient clienteClient) {
        this.repository = repository;
        this.clienteClient = clienteClient;
    }

    public List<ReservaDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    public ReservaDTO obtenerPorId(Long id) {

        Reserva reserva = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        return convertirDTO(reserva);
    }

    public ReservaDTO guardar(ReservaDTO dto) {

        // Verifica que el cliente exista
        ClienteDTO cliente = clienteClient.obtenerCliente(dto.getClienteId());

        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente no encontrado");
        }

        Reserva reserva = convertirEntidad(dto);

        return convertirDTO(repository.save(reserva));
    }

    public ReservaDTO actualizar(Long id, ReservaDTO dto) {

        Reserva reserva = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        clienteClient.obtenerCliente(dto.getClienteId());

        reserva.setClienteId(dto.getClienteId());
        reserva.setServicio(dto.getServicio());
        reserva.setFecha(dto.getFecha());
        reserva.setHora(dto.getHora());
        reserva.setEstado(dto.getEstado());

        return convertirDTO(repository.save(reserva));
    }

    public void eliminar(Long id) {

        Reserva reserva = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        repository.delete(reserva);
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
