package com.src.model;

public class Passenger extends User {
    private Double balance;

    public Passenger() {

    }

    public void addBalance(Double amount) {
        this.balance += amount;
    }

    public void deductBalance(Double amount) {
        this.balance -= amount;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public Double getBalance() {
        return balance;
    }
}