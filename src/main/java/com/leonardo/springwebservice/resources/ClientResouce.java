package com.leonardo.springwebservice.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.leonardo.springwebservice.domain.Client;
import com.leonardo.springwebservice.domain.dto.ClientDTO;
import com.leonardo.springwebservice.domain.dto.ClientNewDTO;
import com.leonardo.springwebservice.services.ClientService;

@RestController
@RequestMapping(value = "/clients")
public class ClientResouce {
    
    @Autowired
    private ClientService clientService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ClientDTO>> findAll() {
        List<Client> list = clientService.findAll();
        List<ClientDTO> listDto = list.stream().map(x -> new ClientDTO(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    } 

    @GetMapping(value = "/{id}")
    public ResponseEntity<Client> findById(@PathVariable Integer id) {
        Client client = clientService.findById(id);
        return ResponseEntity.ok().body(client);
    }

    @GetMapping(value = "/email")
    public ResponseEntity<Client> findByEmail(@RequestParam(value = "value") String email) {
        Client client = clientService.findByEmail(email);
        return ResponseEntity.ok().body(client);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value="/page")
    public ResponseEntity<Page<ClientDTO>> findByPage(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
        @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
        @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<Client> list = clientService.findByPage(page, linesPerPage, orderBy, direction);
        Page<ClientDTO> listDto = list.map(x -> new ClientDTO(x));
        return ResponseEntity.ok().body(listDto);
    }

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody ClientNewDTO clientNewDTO) {
        Client client = clientService.fromDto(clientNewDTO);
        client = clientService.insert(client);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(client.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @Valid @RequestBody ClientDTO clientDto) {
        Client client = clientService.fromDto(clientDto);
        client.setId(id);
        client = clientService.update(client);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/picture")
    public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name = "file") MultipartFile multipartFile) {
        URI uri = clientService.uploadProfilePicture(multipartFile);
        return ResponseEntity.created(uri).build();
    }
}
