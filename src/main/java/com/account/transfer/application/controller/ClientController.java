package com.account.transfer.application.controller;

import com.account.transfer.domain.model.Client;
import com.account.transfer.infrastructure.repository.ClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // GET /clients → lista todos os clientes
    @GetMapping
    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    // GET /clients/{id} → consulta cliente específico
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable String id) {
        return clientRepository.findById(id)
                .map(ResponseEntity::ok)              // retorna 200 + JSON do cliente
                .orElse(ResponseEntity.notFound().build()); // retorna 404 se não existir
    }
}
