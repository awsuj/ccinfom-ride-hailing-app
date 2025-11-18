package com.src.model;

import java.util.ArrayList;

/**
 * Driver model, extends User.
 * FIXED: Removed date fields, added licenseNum.
 */
public class Driver extends User {

    private int driverID;
    private String licenseNum; // ADDED
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private Vehicle currentVehicle;
    private double totalEarnings;
    private boolean isAvailable;

    // ADDED: No-arg constructor
    public Driver() {
        super();
    }

    /**
     * Constructor for creating a new Driver.
     */
    public Driver(int driverID, String name, String email, String password, String phoneNumber) {
        super(name, email, password, phoneNumber);
        this.driverID = driverID;
        this.totalEarnings = 0;
        this.isAvailable = true; // Default availability
    }

    /**
     * Overloaded constructor for loading from DB.
     */
    public Driver(int driverID, String name, String email, String password, String phoneNumber,
                  double totalEarnings, boolean isAvailable) {
        super(name, email, password, phoneNumber);
        this.driverID = driverID;
        this.totalEarnings = (totalEarnings != 0.0) ? totalEarnings : 0.0;
        this.isAvailable = isAvailable;
    }

    public void addEarning(double amount) {
        if (amount > 0) {
            this.totalEarnings += amount;
        }
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        if (this.vehicles == null) {
            this.vehicles = new ArrayList<>();
        }
        this.vehicles.add(vehicle);
    }

    public Vehicle getCurrentVehicle() {
        return currentVehicle;
    }

    public void setCurrentVehicle(Vehicle currentVehicle) {
        this.currentVehicle = currentVehicle;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // ADDED: Getters/Setters for licenseNum
    public String getLicenseNum() {
        return licenseNum;
    }

    public void setLicenseNum(String licenseNum) {
        this.licenseNum = licenseNum;
    }
}