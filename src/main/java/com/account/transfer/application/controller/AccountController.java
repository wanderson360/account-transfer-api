package com.account.transfer.application.controller;

import com.account.transfer.domain.model.Account;
import com.account.transfer.domain.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final TransferService transferService;

    public AccountController(TransferService transferService) {
        this.transferService = transferService;
    }

    // GET /accounts → lista todas
    @GetMapping
    public List<Account> getAccounts() {
        return transferService.listAccounts();
    }

    // GET /accounts/{id} → consulta uma conta específica
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable String id) {
        return transferService.findAccountById(id)
                .map(ResponseEntity::ok)              // retorna 200 + JSON da conta
                .orElse(ResponseEntity.notFound().build()); // retorna 404 se não existir
    }
}
