package com.src.model;

import com.src.enumerations.TransactionStatus;

/**
 * Transaction model.
 * Added getters for all fields.
 */
public class Transaction {
    private int transactionID;
    private int passengerID; // Renamed from customerID for clarity
    private int vehicleID;
    private String pickupLocation;
    private String dropoffLocation;
    private double cost;
    private String time; // Consider using java.time.LocalDateTime instead of String
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

    // --- Added Getters ---

    public int getTransactionID() {
        return transactionID;
    }

    public int getPassengerID() {
        return passengerID;
    }

    /*
    PLACEHOLDER METHOD!!!!
     */
    public int getDriverID() {
        return 1;
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
