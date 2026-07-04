package com.tecsup.service;

import com.tecsup.dto.ClienteDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Cliente;
import com.tecsup.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<ClienteDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO obtenerPorId(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        return convertirDTO(cliente);
    }

    public ClienteDTO guardar(ClienteDTO dto) {

        Cliente cliente = convertirEntidad(dto);

        Cliente guardado = repository.save(cliente);

        return convertirDTO(guardado);
    }

    public ClienteDTO actualizar(Long id, ClienteDTO dto) {

        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setCorreo(dto.getCorreo());
        cliente.setTelefono(dto.getTelefono());

        return convertirDTO(repository.save(cliente));
    }

    public void eliminar(Long id) {

        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        repository.delete(cliente);
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
