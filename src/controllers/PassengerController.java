package com.src.controllers;

import com.src.ViewHandler;
import com.src.database.DriverDAO;
import com.src.database.PassengerDAO;
import com.src.database.TransactionDAO;
import com.src.database.VehicleDAO;
import com.src.enumerations.TransactionStatus;
import com.src.model.Driver;
import com.src.model.Passenger;
import com.src.model.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class PassengerController {

    // --- DAOs ---
    private PassengerDAO passengerDAO;
    private DriverDAO driverDAO;
    private VehicleDAO vehicleDAO;
    private TransactionDAO transactionDAO;

    // --- Core ---
    private ViewHandler viewHandler;
    private static Passenger currentPassenger; // Static to persist login

    // --- FXML Fields (Common) ---
    @FXML private Label passengerNameLabel;
    @FXML private Label balanceLabel;
    @FXML private Button logoutButton;
    @FXML private Button changePasswordButton;

    // --- FXML Fields (PassengerLogin) ---
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button backButton;

    // --- FXML Fields (PassengerMenu) ---
    @FXML private Button rideButton;
    @FXML private Button viewTransactionsButton;

    // --- FXML Fields (PassengerViewRide) ---
    @FXML private Label pickupValue;
    @FXML private Label dropoffValue;
    @FXML private Label driverNameValue; // Corrected
    @FXML private Label vehicleValue;
    @FXML private Label statusValue;
    @FXML private Button cancelRideButton;

    /**
     * Initializes the controller. This is called by the ViewHandler.
     */
    public void init(ViewHandler viewHandler, PassengerDAO pDAO, DriverDAO dDAO, VehicleDAO vDAO, TransactionDAO tDAO) {
        this.viewHandler = viewHandler;
        this.passengerDAO = pDAO;
        this.driverDAO = dDAO;
        this.vehicleDAO = vDAO;
        this.transactionDAO = tDAO;
    }

    /**
     * Called by FXML loader. Used to set up view data.
     */
    @FXML
    public void initialize() {
        if (currentPassenger != null) {
            // This runs for Menu and Ride views
            if (passengerNameLabel != null) {
                passengerNameLabel.setText("Welcome, " + currentPassenger.getName());
            }
            if (balanceLabel != null) {
                balanceLabel.setText(String.format("Balance: P%.2f", currentPassenger.getBalance()));
            }

            // Specific setup for PassengerViewRide
            if (pickupValue != null) {
                populateRideDetails();
            }
        }
    }

    private void populateRideDetails() {
        Transaction ride = currentPassenger.getCurrentTransaction();
        if (ride != null) {
            Driver driver = driverDAO.findByID(ride.getDriverID());
            com.src.model.Vehicle vehicle = vehicleDAO.findByID(ride.getVehicleID());

            pickupValue.setText(ride.getPickupLocation());
            dropoffValue.setText(ride.getDropoffLocation());
            driverNameValue.setText(driver != null ? driver.getName() : "N/A");
            vehicleValue.setText(vehicle != null ? vehicle.getModel() + " (" + vehicle.getPlateNumber() + ")" : "N/A");
            statusValue.setText(ride.getStatus().toString());
        } else {
            // Handle no current ride
            pickupValue.setText("N/A");
            dropoffValue.setText("N/A");
            driverNameValue.setText("N/A");
            vehicleValue.setText("N/A");
            statusValue.setText("N/A");
            cancelRideButton.setDisable(true);
        }
    }

    // --- Event Handlers ---

    @FXML
    void onLoginClicked(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        Passenger p = passengerDAO.findByEmail(email);

        if (p != null && p.checkPassword(password)) {
            System.out.println("Passenger login successful: " + p.getName());
            currentPassenger = p; // Set the logged-in passenger

            // Load history
            List<Transaction> transactions = transactionDAO.findByPassenger(p.getPassengerID());
            transactions.forEach(currentPassenger::addTransactionToHistory);
            // TODO: Find ONGOING transaction and set as current

            try {
                viewHandler.showPassengerMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid login");
            // TODO: Show alert
        }
    }

    @FXML
    void onBackClicked(ActionEvent event) throws IOException {
        viewHandler.showAuthView();
    }

    @FXML
    void onLogoutClicked(ActionEvent event) throws IOException {
        currentPassenger = null; // Log out
        viewHandler.showAuthView();
    }

    @FXML
    void onBookRideClicked(ActionEvent event) {
        if (currentPassenger.getCurrentTransaction() != null) {
            System.out.println("You already have an ongoing ride!");
            // TODO: Show alert and switch to ride view
            try {
                viewHandler.showPassengerRideView();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // --- Mock Ride Booking Logic ---
        // TODO: This should be in a separate "Book Ride" view with text fields
        String pickupLocation = "Start Point";
        String dropoffLocation = "End Point";

        List<Driver> drivers = driverDAO.getAllDrivers();
        Driver selectedDriver = drivers.stream().filter(Driver::isAvailable).findFirst().orElse(null);

        if (selectedDriver == null) {
            System.out.println("No available drivers.");
            // TODO: Show alert
            return;
        }

        double cost = 80 + new Random().nextDouble() * (500 - 80);

        if (currentPassenger.getBalance() < cost) {
            System.out.println("Insufficient balance.");
            // TODO: Show alert
            return;
        }

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        int newTransactionID = (int) (transactionDAO.findByPassenger(currentPassenger.getPassengerID()).size() + 1); // Mock ID

        Transaction newTransaction = new Transaction(
                newTransactionID,
                currentPassenger.getPassengerID(),
                selectedDriver.getDriverID(),
                selectedDriver.getCurrentVehicle().getVehicleID(),
                pickupLocation,
                dropoffLocation,
                cost,
                time
        );

        // Update objects
        currentPassenger.setCurrentTransaction(newTransaction);
        currentPassenger.addTransactionToHistory(newTransaction);
        currentPassenger.deductBalance(cost); // Deduct balance

        selectedDriver.setCurrentTransaction(newTransaction);
        selectedDriver.setAvailable(false);

        // Update DB
        transactionDAO.addTransaction(newTransaction);
        passengerDAO.updatePassenger(currentPassenger);
        driverDAO.updateDriver(selectedDriver);

        System.out.println("Ride booked successfully.");

        // Switch to ride view
        try {
            viewHandler.showPassengerRideView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onCancelRideClicked(ActionEvent event) {
        Transaction ride = currentPassenger.getCurrentTransaction();
        if (ride != null) {
            ride.setStatus(TransactionStatus.CANCELLED);

            // Refund (or partial refund)
            currentPassenger.addBalance(ride.getCost());

            Driver driver = driverDAO.findByID(ride.getDriverID());
            if (driver != null) {
                driver.removeCurrentTransaction();
                driver.setAvailable(true);
                driverDAO.updateDriver(driver);
            }

            currentPassenger.removeCurrentTransaction();

            transactionDAO.updateTransaction(ride);
            passengerDAO.updatePassenger(currentPassenger);

            System.out.println("Ride cancelled.");
            populateRideDetails(); // Refresh view
        }
    }

    // TODO: Implement other handlers
}