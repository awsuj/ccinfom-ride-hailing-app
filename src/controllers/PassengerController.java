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
import com.src.model.Vehicle; // Import Vehicle
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*; // Import TableView

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * FIXED: This controller is fully wired.
 * - All FXML fields are included.
 * - All handlers are included.
 * - Logic is updated for the new Transaction model (no driverID).
 */
public class PassengerController {

    // --- DAOs ---
    private PassengerDAO passengerDAO;
    private DriverDAO driverDAO;
    private VehicleDAO vehicleDAO;
    private TransactionDAO transactionDAO;

    // --- Core ---
    private ViewHandler viewHandler;
    private static Passenger currentPassenger;

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
    @FXML private Label driverNameValue;
    @FXML private Label vehicleValue;
    @FXML private Label statusValue;
    @FXML private Button cancelRideButton;

    // --- FXML Fields (PassengerManageAccount) ---
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private Button saveButton;

    // --- FXML Fields (PassengerChangePassword) ---
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button updatePasswordButton;

    // --- FXML Fields (PassengerViewTransactions) ---
    @FXML private TableView<Transaction> transactionsTable;

    /**
     * Initializes the controller.
     */
    public void init(ViewHandler viewHandler, PassengerDAO pDAO, DriverDAO dDAO, VehicleDAO vDAO, TransactionDAO tDAO) {
        this.viewHandler = viewHandler;
        this.passengerDAO = pDAO;
        this.driverDAO = dDAO;
        this.vehicleDAO = vDAO;
        this.transactionDAO = tDAO;
    }

    @FXML
    public void initialize() {
        if (currentPassenger != null) {
            if (passengerNameLabel != null) {
                passengerNameLabel.setText("Welcome, " + currentPassenger.getName());
            }
            if (balanceLabel != null) {
                balanceLabel.setText(String.format("Balance: P%.2f", currentPassenger.getBalance()));
            }
            if (pickupValue != null) {
                populateRideDetails();
            }
        }
    }

    private void populateRideDetails() {
        Transaction ride = currentPassenger.getCurrentTransaction();
        if (ride != null) {
            // Find the vehicle
            Vehicle vehicle = vehicleDAO.findByID(ride.getVehicleID());
            Driver driver = null;
            if (vehicle != null) {
                // Find the driver using the vehicle's driverID
                driver = driverDAO.findByID(vehicle.getDriverID());
            }

            pickupValue.setText(ride.getPickupLocation());
            dropoffValue.setText(ride.getDropoffLocation());
            driverNameValue.setText(driver != null ? driver.getName() : "N/A");
            // FIXED: Use getModelName()
            vehicleValue.setText(vehicle != null ? vehicle.getModelName() + " (" + vehicle.getPlateNumber() + ")" : "N/A");
            statusValue.setText(ride.getStatus().toString());
        } else {
            pickupValue.setText("N/A");
            dropoffValue.setText("N/A");
            driverNameValue.setText("N/A");
            vehicleValue.setText("N/A");
            statusValue.setText("N/A");
            if(cancelRideButton != null) cancelRideButton.setDisable(true);
        }
    }

    @FXML
    void onLoginClicked(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        Passenger p = passengerDAO.findByEmail(email);

        if (p != null && p.checkPassword(password)) {
            System.out.println("Passenger login successful: " + p.getName());
            currentPassenger = p;

            List<Transaction> transactions = transactionDAO.findByPassenger(p.getPassengerID());
            transactions.forEach(currentPassenger::addTransactionToHistory);

            // FIXED: Find ONGOING transaction
            for (Transaction t : transactions) {
                if (t.getStatus() == TransactionStatus.ONGOING) {
                    currentPassenger.setCurrentTransaction(t);
                    break;
                }
            }

            try {
                viewHandler.showPassengerMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid login");
        }
    }

    @FXML
    void onBookRideClicked(ActionEvent event) {
        if (currentPassenger.getCurrentTransaction() != null) {
            System.out.println("You already have an ongoing ride!");
            try {
                viewHandler.showPassengerRideView();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        String pickupLocation = "Start Point";
        String dropoffLocation = "End Point";

        List<Driver> drivers = driverDAO.getAllDrivers();
        Driver selectedDriver = drivers.stream().filter(Driver::isAvailable).findFirst().orElse(null);

        if (selectedDriver == null) {
            System.out.println("No available drivers.");
            return;
        }

        if (selectedDriver.getVehicles() == null || selectedDriver.getVehicles().isEmpty()) {
            selectedDriver.setVehicles((ArrayList<Vehicle>) vehicleDAO.findAllByDriverID(selectedDriver.getDriverID()));
        }

        if (selectedDriver.getCurrentVehicle() == null && !selectedDriver.getVehicles().isEmpty()) {
            selectedDriver.setCurrentVehicle(selectedDriver.getVehicles().get(0));
        }

        if (selectedDriver.getCurrentVehicle() == null) {
            System.out.println("Selected driver has no vehicle.");
            return;
        }

        double cost = 80 + new Random().nextDouble() * (500 - 80);

        if (currentPassenger.getBalance() < cost) {
            System.out.println("Insufficient balance.");
            return;
        }

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        int newTransactionID = (int) (System.currentTimeMillis() % 10000); // Mock ID

        // FIXED: Constructor call no longer includes driverID
        Transaction newTransaction = new Transaction(
                newTransactionID,
                currentPassenger.getPassengerID(),
                selectedDriver.getCurrentVehicle().getVehicleID(),
                pickupLocation,
                dropoffLocation,
                cost,
                time
        );

        currentPassenger.setCurrentTransaction(newTransaction);
        currentPassenger.addTransactionToHistory(newTransaction);
        currentPassenger.deductBalance(cost);

        selectedDriver.setCurrentTransaction(newTransaction);
        selectedDriver.setAvailable(false);

        transactionDAO.addTransaction(newTransaction);
        passengerDAO.updatePassengerBalance(currentPassenger); // FIXED: Use balance-specific update
        driverDAO.updateDriver(selectedDriver);

        System.out.println("Ride booked successfully.");

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
            currentPassenger.addBalance(ride.getCost()); // Refund

            Vehicle vehicle = vehicleDAO.findByID(ride.getVehicleID());
            if (vehicle != null) {
                Driver driver = driverDAO.findByID(vehicle.getDriverID());
                if (driver != null) {
                    driver.removeCurrentTransaction();
                    driver.setAvailable(true);
                    driverDAO.updateDriver(driver);
                }
            }

            currentPassenger.removeCurrentTransaction();
            transactionDAO.updateTransaction(ride);
            passengerDAO.updatePassengerBalance(currentPassenger); // FIXED: Use balance-specific update

            System.out.println("Ride cancelled.");
            populateRideDetails(); // Refresh view
        }
    }

    // --- Other Handlers ---

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
    void onManageAccountClicked(ActionEvent event) throws IOException {
        viewHandler.showPassengerManageAccount();
    }

    @FXML
    void onChangePasswordClicked(ActionEvent event) throws IOException {
        viewHandler.showPassengerChangePassword();
    }

    @FXML
    void onViewTransactionsClicked(ActionEvent event) throws IOException {
        viewHandler.showPassengerTransactions();
    }

    @FXML
    void onSaveClicked(ActionEvent event) {
        System.out.println("Saving account...");
        // TODO: Get text from nameField, emailField, etc. and call passengerDAO.updatePassenger()
    }

    @FXML
    void onUpdatePasswordClicked(ActionEvent event) {
        System.out.println("Updating password...");
        // TODO: Get passwords, check, and call passenger.updatePassword() & passengerDAO.updatePassenger()
    }

    @FXML
    void onBackToMenuClicked(ActionEvent event) throws IOException {
        viewHandler.showPassengerMenu();
    }
}