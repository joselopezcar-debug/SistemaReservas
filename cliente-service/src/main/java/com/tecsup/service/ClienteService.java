package com.tecsup.service;

import com.tecsup.dto.ClienteDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Cliente;
import com.tecsup.repository.ClienteRepository;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<ClienteDTO> listar() {

        logger.info("Listando todos los clientes");

        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO obtenerPorId(Long id) {

        logger.info("Buscando cliente con id {}", id);

        Cliente cliente = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        return convertirDTO(cliente);
    }

    public ClienteDTO guardar(ClienteDTO dto) {

        logger.info("Registrando nuevo cliente: {} {}",
                dto.getNombre(), dto.getApellido());

        Cliente cliente = convertirEntidad(dto);

        Cliente guardado = repository.save(cliente);

        logger.info("Cliente registrado correctamente con id {}",
                guardado.getId());

        return convertirDTO(guardado);
    }

    public ClienteDTO actualizar(Long id, ClienteDTO dto) {

        logger.info("Actualizando cliente con id {}", id);

        Cliente cliente = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setCorreo(dto.getCorreo());
        cliente.setTelefono(dto.getTelefono());

        Cliente actualizado = repository.save(cliente);

        logger.info("Cliente {} actualizado correctamente", id);

        return convertirDTO(actualizado);
    }

    public void eliminar(Long id) {

        logger.info("Eliminando cliente con id {}", id);

        Cliente cliente = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        repository.delete(cliente);

        logger.info("Cliente {} eliminado correctamente", id);
    }

    private ClienteDTO convertirDTO(Cliente cliente) {

        return new ClienteDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getCorreo(),
                cliente.getTelefono()
        );
    }

    private Cliente convertirEntidad(ClienteDTO dto) {

        Cliente cliente = new Cliente();

        cliente.setId(dto.getId());
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setCorreo(dto.getCorreo());
        cliente.setTelefono(dto.getTelefono());

        return cliente;
    }
}
