package com.src.model;

/**
 * Vehicle model.
 * Fixed: Made fields private and added getters.
 */
public class Vehicle {
    private int vehicleID;
    private int driverID; // Added driverID as it's likely a foreign key
    private String registration_num;
    private String carBrand;
    private String model;
    private String plateNumber;
    private String color;
    private int numberOfSeats;
    private String fuelType;
    private int yearAcquired;

    public Vehicle(int vehicleID, int driverID, String model, String plateNumber, String color) {
        this.vehicleID = vehicleID;
        this.driverID = driverID;
        this.model = model;
        this.plateNumber = plateNumber;
        this.color = color;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public int getDriverID() {
        return driverID;
    }

    public String getModelName() {
        return model;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getColor() {
        return color;
    }

    public String getRegistrationNum() {
        return registration_num;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getFuelType() {
        return fuelType;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public String getYearAcquired() {
        return yearAcquired;
    }

    public String getPlateNum() {
        return plateNumber;
    }
}