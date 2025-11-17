package com.src.model;

public class Driver extends User {
    private int driverID;
    private ArrayList<Vehicle> vehicles;
    private Vehicle currentVehicle;
    private double totalEarnings;
    private boolean isAvailable;

    public Driver(int driverID) {
        this.driverID = driverID;
        this.totalEarnings = 0;
        this.isAvailable = true;
    }

    public Driver(int driverID, ArrayList<Vehicle> vehicles,
                  double totalEarnings, boolean isAvailable) {
        this.driverID = driverID;
        this.vehicles = vehicles;
        this.totalEarnings = totalEarnings;
        this.isAvailable = isAvailable;
    }

    public void addEarning(double amount) {
        this.totalEarnings += amount;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public Vehicle getCurrentVehicle() {
        return
    }
}