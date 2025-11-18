package com.src.model;

import com.src.enumerations.TransactionStatus;

public class Transaction {
    private int transactionID;
    private int passengerID;
    private int vehicleID;
    private String pickupLocation;
    private String dropoffLocation;
    private double cost;
    private String time;
    private TransactionStatus status;

    public Transaction(int transactionID, int passengerID, int vehicleID,
                       String pickupLocation, String dropoffLocation, double cost, String time) {
        this.transactionID = transactionID;
        this.passengerID = passengerID;
        this.vehicleID = vehicleID;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.cost = cost;
        this.time = time;
        this.status = TransactionStatus.ONGOING; // Default status
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public TransactionStatus getStatus() {
        return status;
    }
    
    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    // --- Getters ---
    public int getTransactionID() {
        return transactionID;
    }

    public int getPassengerID() {
        return passengerID;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public double getCost() {
        return cost;
    }

    public String getTime() {
        return time;
    }
}