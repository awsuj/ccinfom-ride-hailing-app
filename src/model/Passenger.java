package com.src.model;

public class Passenger extends User {
    private String occupation;
    private Double balance = 0.0;
    private int passengerID;

    public Passenger() {
        super(); // Calls no-arg User constructor
    }

    /**
     * Constructor for creating a new Passenger.
     */
    public Passenger(int passengerID, String name, String email, String password, String phoneNumber) {
        super(name, email, password, phoneNumber);
        this.passengerID = passengerID;
        this.balance = 0.0; // Default balance
    }

    /**
     * Overloaded constructor for loading from DB.
     * This is used by the DAO.
     */
    public Passenger(int passengerID, String name, String email, String password, String phoneNumber, Double balance) {
        super(name, email, password, phoneNumber);
        this.passengerID = passengerID;
        this.balance = (balance != null) ? balance : 0.0;
    }

    public void addBalance(Double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public void deductBalance(Double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
        }
    }

    @Override
    public void updatePassword(String password) {
        super.setPassword(password);
    }

    public Double getBalance() {
        return balance;
    }

    public int getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(int passengerID) {
        this.passengerID = passengerID;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}