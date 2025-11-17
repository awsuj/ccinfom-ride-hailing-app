package com.src.model;

/**
 * Vehicle model.
 * Fixed: Made fields private and added getters.
 */
public class Vehicle {
    private int vehicleID;
    private int driverID; // Added driverID as it's likely a foreign key
    private String model;
    private String plateNumber;
    private String color;

    public Vehicle(int vehicleID, int driverID, String model, String plateNumber, String color) {
        this.vehicleID = vehicleID;
        this.driverID = driverID;
        this.model = model;
        this.plateNumber = plateNumber;
        this.color = color;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public int getDriverID() {
        return driverID;
    }

    public String getModel() {
        return model;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getColor() {
        return color;
    }
}