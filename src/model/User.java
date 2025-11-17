package com.src.model;

import java.util.ArrayList;

/**
 * Base class for all users (Passengers and Drivers).
 * Made fields protected so subclasses can access them.
 */
public class User {
    protected String name;
    protected String email;
    protected String password;
    protected String phoneNumber;
    private Transaction currentTransaction;
    private ArrayList<Transaction> transactionHistory = new ArrayList<>();

    public User(String name, String email, String password, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Fixed: Return type changed to boolean
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Fixed: Return type changed to void
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransactionToHistory(Transaction transaction) {
        if (this.transactionHistory == null) {
            this.transactionHistory = new ArrayList<>();
        }
        this.transactionHistory.add(transaction);
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public void removeCurrentTransaction() {
        this.currentTransaction = null;
    }
}