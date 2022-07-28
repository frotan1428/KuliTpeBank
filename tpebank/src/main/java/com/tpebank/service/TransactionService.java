package com.tpebank.service;

import com.tpebank.domain.Transaction;
import com.tpebank.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;

    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

}
