package com.src.controllers;

import com.src.model.*;

public class PassengerController {
    private PassengerView view;
    private ArrayList<drivers> drivers;
    private Passenger currentUser;
    private ArrayList<Transaction> transactionHistory;
    private TransactionDAO transactionDAO;
    private PassengerDAO passengerDAO;
    private DriverDAO driverDAO;
    private VehicleDAO vehicleDAO;

    public PassengerController() {

    }

    public boolean passengerLogin(String email, String password) {
        Passsenger p = passengerDAO.findByEmail(email);

        if (p != null && p.checkPassword(password)) {
            this.currentUser = p;
            return true;
        }

        return false;
    }

    public void updatePassword(String newPassword) {
        currentUser.updatePassword(newPassword);
    }

    public void bookRide(String pickupLocation, String dropoffLocation, double cost) {
        int driverID;

        do {
            Driver selectedDriver = drivers.get(randomIndex);
            // update to number of existing drivers
        } while (selectedDriver.get(id).isAvailable() == false);

        driverID = selectedDriver.getID();
        int vehicleID = drivers.get(id).getCurrentVehicle().getVehicleID();

        double cost = 80 + Math.random() * (500 - 80);

        String time = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Transaction newTransaction = new Transaction(transactionHistory.size() + 1, currentUser.getID(), driverID,
                vehicleID, pickupLocation, dropoffLocation, cost, time, TransactionStatus.ONGOING);

        transactionHistory.add(newTransaction);
        currentUser.addTransaction(newTransaction);
        drivers.getDriver(driverID).addTransaction(newTransaction);
        transactionDAO.addTransaction(newTransaction);

        selectedDriver.setAvailability(false);

        System.out.println("Ride booked successfully.");
    }
}