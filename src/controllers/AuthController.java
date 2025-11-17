package com.src.controllers;

public class AuthController {

    private PassengerDAO passengerDAO;
    private DriverDAO driverDAO;
    private VehicleDAO vehicleDAO;
    private TransactionDAO transactionDAO;

    public AuthController(
            PassengerDAO passengerDAO,
            DriverDAO driverDAO,
            VehicleDAO vehicleDAO,
            TransactionDAO transactionDAO
    ) {
        this.passengerDAO = passengerDAO;
        this.driverDAO = driverDAO;
        this.vehicleDAO = vehicleDAO;
        this.transactionDAO = transactionDAO;
    }

    // starting point of the app
    public void start() {
        // call view
    }

    // selects passenger login, returns a PassengerController ready to use
    public PassengerController selectPassengerLogin() {
        return new PassengerController(
                passengerDAO,
                driverDAO,
                vehicleDAO,
                transactionDAO
        );
    }

    // selects driver login, returns a DriverController ready to use
    public DriverController selectDriverLogin() {
        return new DriverController(
                driverDAO,
                vehicleDAO,
                transactionDAO
        );
    }
}
