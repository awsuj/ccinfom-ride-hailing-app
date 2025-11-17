package com.src.model;

public class User {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private Transaction currentTransaction
    private ArrayList<Transaction> transactionHistory;

    public User(String name, String password, String phoneNumber) {
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String checkPassword(String password) {
        if (password.equals(this.password)) {
            return true;
        }

        return false;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void removeCurrentTransaction() {
        this.currentTransaction = null;
    }

    public String updatePassword(String newPassword) {
        password = newPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}