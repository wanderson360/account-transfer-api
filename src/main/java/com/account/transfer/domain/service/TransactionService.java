package com.account.transfer.domain.service;

import com.account.transfer.domain.model.Account;
import com.account.transfer.domain.model.Transaction;
import com.account.transfer.infrastructure.repository.AccountRepository;
import com.account.transfer.infrastructure.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final KafkaProducerService kafkaProducerService;

    public TransactionService(AccountRepository accountRepository,
                              TransactionRepository transactionRepository,
                              KafkaProducerService kafkaProducerService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Transactional
    public Transaction transfer(String originId, String destinationId, BigDecimal amount) {
        Account origin = accountRepository.findById(originId)
                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"));
        Account destination = accountRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"));

        if (origin.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        // Atualiza saldos
        origin.setBalance(origin.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));

        accountRepository.save(origin);
        accountRepository.save(destination);

        // Cria transação
        Transaction transaction = new Transaction();
        transaction.setId("tx-" + System.currentTimeMillis());
        transaction.setOriginAccount(origin);
        transaction.setDestinationAccount(destination);
        transaction.setAmount(amount);
        transaction.setCreatedAt(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Publica a transação no Kafka após salvar com sucesso
        kafkaProducerService.publishTransaction(savedTransaction);
        
        return savedTransaction;
    }

    public List<Transaction> listAll() {
    return transactionRepository.findAll();
    }

    public Optional<Transaction> findById(String id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> searchTransactions(String clientId, LocalDateTime startDate, LocalDateTime endDate) {
    List<Transaction> all = transactionRepository.findAll();

    return all.stream()
            .filter(tx -> clientId == null ||
                    tx.getOriginAccount().getClient().getId().equals(clientId) ||
                    tx.getDestinationAccount().getClient().getId().equals(clientId))
            .filter(tx -> startDate == null || !tx.getCreatedAt().isBefore(startDate))
            .filter(tx -> endDate == null || !tx.getCreatedAt().isAfter(endDate))
            .toList();
}

}


