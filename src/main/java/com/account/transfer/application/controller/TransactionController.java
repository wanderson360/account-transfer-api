package com.account.transfer.application.controller;

import com.account.transfer.domain.model.Transaction;
import com.account.transfer.domain.service.TransactionService;
import com.account.transfer.application.dto.TransactionRequest;
import com.account.transfer.application.dto.TransactionSearchRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // GET /transactions → lista todas
    @GetMapping
    public List<Transaction> getTransactions() {
        return transactionService.listAll();
    }

    // GET /transactions/{id} → consulta transação específica
    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable String id) {
        return transactionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));
    }

    // POST /transactions → cria nova
    @PostMapping
    public Transaction createTransaction(@RequestBody TransactionRequest request) {
        return transactionService.transfer(
                request.getOriginId(),
                request.getDestinationId(),
                request.getAmount()
        );
    }

    // POST /transactions/search → filtra por cliente ou intervalo de datas
    @PostMapping("/search")
    public List<Transaction> searchTransactions(@RequestBody TransactionSearchRequest request) {
        return transactionService.searchTransactions(
                request.getClientId(),
                request.getStartDate(),
                request.getEndDate()
        );
    }
}
