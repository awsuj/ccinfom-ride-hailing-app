package com.src.model;

public class Transaction {
    private int transactionID;
    private int customerID;
    private int driverID;
    private int vehicleID;
    private String pickupLocation;
    private String dropoffLocation;
    private double cost;
    private String time;

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
    }
}