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
    protected Transaction currentTransaction;
    protected ArrayList<Transaction> transactionHistory = new ArrayList<>();

    protected String gender;
    protected int age;

    public User() {}

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

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}