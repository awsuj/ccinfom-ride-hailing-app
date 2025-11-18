package com.src.database;

import java.util.List;
import com.src.model.Transaction;

public interface TransactionDAO {

    void addTransaction(Transaction transaction);

    Transaction geTransactionByID(int id);

    List<Transaction> getAllTransaction();

    void updateTransactionsStatus(int id, String status);

    void deleteTransaction(int id);
}
