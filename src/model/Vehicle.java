package com.src.model;

public class Vehicle {
    private int vehicleID;
    public String model;
    public String plateNumber;
    public String color;

    public Vehicle(int vehicleID, String model, String plateNumber, String color) {
        this.vehicleID = vehicleID;
        this.model = model;
        this.plateNumber = plateNumber;
        this.color = color;
    }

    public int getVehicleID() {
        return vehicleID;
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