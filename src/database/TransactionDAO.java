package com.src.database;

import com.src.model.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Placeholder implementation of TransactionDAO.
 * Stores data in memory.
 */
public class TransactionDAO {
    private Map<Integer, Transaction> transactions = new HashMap<>();
    private int nextId = 1;

    public void addTransaction(Transaction newTransaction) {
        System.out.println("DAO: Adding transaction ID " + newTransaction.getTransactionID());
        transactions.put(newTransaction.getTransactionID(), newTransaction);
    }

    public Transaction findByID(int transactionID) {
        return transactions.get(transactionID);
    }

    public List<Transaction> findByPassenger(int passengerID) {
        return transactions.values().stream()
                .filter(t -> t.getPassengerID() == passengerID)
                .collect(Collectors.toList());
    }

    public List<Transaction> findByDriver(int driverID) {
        // Your Driver model uses a String licenseNum, but the Transaction model
        // and this DAO interface use an int driverID. This might be a mismatch
        // to resolve later when you build the real TransactionDAOImpl.
        // For now, this will work with the placeholder DriverDAOImpl.
        return transactions.values().stream()
                .filter(t -> t.getDriverID() == driverID)
                .collect(Collectors.toList());
    }

    public void updateTransaction(Transaction transaction) {
        System.out.println("DAO: Updating transaction " + transaction.getTransactionID());
        transactions.put(transaction.getTransactionID(), transaction);
    }
}