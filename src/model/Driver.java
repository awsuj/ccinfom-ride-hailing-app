package com.src.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Driver model, extends User.
 */
public class Driver extends User {

    private int driverID;

    // Fixed: Initialized ArrayList
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private Vehicle currentVehicle;
    private double totalEarnings;
    private boolean isAvailable;

    // Added:
    private Date dateOfEmployment;
    private Date dateOfResignation;

    /**
     * Constructor for creating a new Driver.
     */
    public Driver(int driverID, String name, String email, String password, String phoneNumber) {
        // Fixed: Added super() call
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
        this.totalEarnings = totalEarnings;
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

    // Fixed: Completed method
    public Vehicle getCurrentVehicle() {
        return currentVehicle;
    }

    public void setCurrentVehicle(Vehicle currentVehicle) {
        this.currentVehicle = currentVehicle;
    }

    public int getDriverID() {
        return driverID;
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

    // Added dates data:
    public Date getDateOfEmployment() {
        return this.dateOfEmployment;
    }

    public Date getDateOfResignation() {
        return this.dateOfResignation;
    }
}
