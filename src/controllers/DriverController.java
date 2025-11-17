package com.src.controllers;

import com.src.model.*;
import com.src.enumerations.*;

public class DriverController {
    private DriverView view;
    private Driver currentDriver;

    // DAOs
    private TransactionDAO transactionDAO;
    private DriverDAO driverDAO;
    private VehicleDAO vehicleDAO;

    public boolean driverLogin(String email, String password) {
        Driver d = driverDAO.findByEmail(email);

        if (d != null && d.checkPassword(password)) {
            this.currentDriver = d;
            return true;
        }

        return false;
    }

    public void updatePassword(String newPassword) {
        if (this.currentDriver.checkPassword(newPassword) == false) {
            currentDriver.setPassword(newPassword);
            driverDAO.updateDrive(currentDriver);
        }
    }

    public Transaction viewCurrentRide() {
        return currentDriver.getCurrentTransaction();
    }

    public double viewEarnings() {
        return currentDriver.getEarnings();
    }

    public void dropoffPassenger() {
        if (currentDriver.getCurrentTransaction() == null)
            return;


    }

    public addVehicle(String model, String plateNumber, String color) {
        new Vehicle vehicle = Vehicle(transactionDAO.)
    }

    public ArrayList<Vehicle> viewVehicles() {
        return currentDriver.getVehicles();
    }
}