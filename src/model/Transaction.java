package com.src.model;

import com.src.enumerations.*;

public class Transaction {
    private int transactionID;
    private int customerID;
    private int driverID;
    private int vehicleID;
    private String pickupLocation;
    private String dropoffLocation;
    private double cost;
    private String time;
    private TransactionStatus status;

    public Transaction(int transactionID, int customerID, int driverID, int vehicleID,
                       String pickupLocation, String dropoffLocation, double cost, String time) {
        this.transactionID = transactionID;
        this.customerID = customerID;
        this.driverID = driverID;
        this.vehicleID = vehicleID;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.cost = cost;
        this.time = time;
        this.status = TransactionStatus.ONGOING;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public TransactionStatus getStatus() {
        return status;
    }
}