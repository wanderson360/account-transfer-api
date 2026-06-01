package com.account.transfer.domain.service;

import com.account.transfer.domain.model.Account;
import com.account.transfer.infrastructure.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> listAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> findAccountById(String id) {
    return accountRepository.findById(id);
}

}
