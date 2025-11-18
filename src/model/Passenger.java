package com.src.model;

/**
 * Passenger model, extends User.
 */
public class Passenger extends User {
    private String occupation;

    // Fixed: Initialized balance to prevent NullPointerException
    private Double balance = 0.0;
    private int passengerID; // Added passengerID based on DAO

    public Passenger() {
    }

    /**
     * Constructor for creating a new Passenger.
     */
    public Passenger(int passengerID, String name, String email, String password, String phoneNumber) {
        // Fixed: Added super() call to initialize base User fields
        super(name, email, password, phoneNumber);
        this.passengerID = passengerID;
        this.balance = 0.0; // Default balance
    }

    /**
     * Overloaded constructor for loading from DB.
     */
    public Passenger(int passengerID, String name, String email, String password, String phoneNumber, Double balance) {
        super(name, email, password, phoneNumber);
        this.passengerID = passengerID;
        this.balance = balance;
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

    // This method is already in the User class, but if you want to override:
    @Override
    public void updatePassword(String password) {
        // Use the setter from the base class
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