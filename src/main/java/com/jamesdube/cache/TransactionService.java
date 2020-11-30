package com.jamesdube.cache;

import java.util.List;

public interface TransactionService {

    void save(Transaction transaction);

    void save(List<Transaction> transactionList);

    void deleteAll();

    Transaction findById(Long id);
}
