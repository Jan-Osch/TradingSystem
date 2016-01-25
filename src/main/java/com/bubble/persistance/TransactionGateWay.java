package com.bubble.persistance;

import com.bubble.transactions.entities.Transaction;

import java.util.UUID;

public interface TransactionGateWay {
    void saveTransaction(Transaction transaction);

    Transaction getTransactionByUuid(UUID uuid);

    Iterable<Transaction> getTransactionsForUser(UUID userUuid);
}
