package com.src.controllers;

import com.src.ViewHandler;
import com.src.database.DriverDAO;
import com.src.database.PassengerDAO; // ADDED
import com.src.database.TransactionDAO;
import com.src.database.VehicleDAO;
import com.src.model.Driver;
import com.src.model.Transaction;
import com.src.model.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart; // ADDED
import javafx.scene.control.*; // ADDED
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DriverController {

    // --- DAOs ---
    private DriverDAO driverDAO;
    private VehicleDAO vehicleDAO;
    private TransactionDAO transactionDAO;
    private PassengerDAO passengerDAO; // ADDED

    // --- Core ---
    private ViewHandler viewHandler;
    private static Driver currentDriver; // Static to persist login across views

    // --- FXML Fields (Common) ---
    @FXML private Label driverNameLabel;
    @FXML private Label balanceLabel;
    @FXML private Button logoutButton;
    @FXML private Button changePasswordButton;

    // --- FXML Fields (DriverLogin) ---
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button backButton;

    // --- FXML Fields (DriverMenu) ---
    @FXML private Button viewRideButton;
    @FXML private Button manageAccountButton;
    @FXML private Button viewTransactionsButton;
    @FXML private Button viewVehicleButton;
    @FXML private Button viewReportsButton;

    // --- FXML Fields (DriverViewRide) ---
    @FXML private Label pickupValue;
    @FXML private Label dropoffValue;
    @FXML private Label passengerNameValue;
    @FXML private Label fareValue;
    @FXML private Label statusValue;
    @FXML private Button dropoffButton;

    // --- FXML Fields (DriverManageAccount) ---
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private Button saveButton;

    // --- FXML Fields (DriverChangePassword) ---
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button updatePasswordButton;

    // --- FXML Fields (DriverViewVehicle) ---
    @FXML private Label plateValue;
    @FXML private Label modelValue;
    @FXML private Label brandValue;
    @FXML private Label yearValue;
    @FXML private Label fuelValue;

    // --- FXML Fields (DriverViewTransactions) ---
    @FXML private TableView<Transaction> transactionsTable;

    // --- FXML Fields (DriverViewReports) ---
    @FXML private BarChart<?, ?> incomeChart;

    /**
     * Initializes the controller. This is called by the ViewHandler.
     * FIXED: Added PassengerDAO
     */
    public void init(ViewHandler viewHandler, DriverDAO driverDAO, VehicleDAO vehicleDAO, TransactionDAO transactionDAO, PassengerDAO passengerDAO) {
        this.viewHandler = viewHandler;
        this.driverDAO = driverDAO;
        this.vehicleDAO = vehicleDAO;
        this.transactionDAO = transactionDAO;
        this.passengerDAO = passengerDAO; // ADDED
    }

    @FXML
    public void initialize() {
        if (currentDriver != null) {
            if (driverNameLabel != null) {
                driverNameLabel.setText("Driver: " + currentDriver.getName());
            }
            if (balanceLabel != null) {
                balanceLabel.setText(String.format("Earnings: P%.2f", currentDriver.getTotalEarnings()));
            }
            if (pickupValue != null) {
                populateRideDetails();
            }
        }
    }

    /**
     * FIXED: Now finds passenger name using passengerDAO
     */
    private void populateRideDetails() {
        Transaction ride = currentDriver.getCurrentTransaction();
        if (ride != null) {
            pickupValue.setText(ride.getPickupLocation());
            dropoffValue.setText(ride.getDropoffLocation());

            // Find passenger name
            com.src.model.Passenger p = passengerDAO.findByID(ride.getPassengerID());
            passengerNameValue.setText(p != null ? p.getName() : "Unknown");

            fareValue.setText(String.format("P%.2f", ride.getCost()));
            statusValue.setText(ride.getStatus().toString());
        } else {
            pickupValue.setText("N/A");
            dropoffValue.setText("N/A");
            passengerNameValue.setText("N/A");
            fareValue.setText("N/A");
            statusValue.setText("N/A");
            if(dropoffButton != null) dropoffButton.setDisable(true);
        }
    }

    // --- Event Handlers ---

    @FXML
    void onLoginClicked(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email or password empty");
            return;
        }

        Driver d = driverDAO.findByEmail(email);

        if (d != null && d.checkPassword(password)) {
            System.out.println("Driver login successful: " + d.getName());
            currentDriver = d;

            // FIXED: Use findAllByDriverID to prevent crash
            currentDriver.setVehicles((ArrayList<Vehicle>) vehicleDAO.findAllByDriverID(d.getDriverID()));

            // FIXED: Set a default vehicle
            if (!currentDriver.getVehicles().isEmpty()) {
                currentDriver.setCurrentVehicle(currentDriver.getVehicles().get(0));
            }

            List<Transaction> transactions = transactionDAO.findByDriver(d.getDriverID());
            transactions.forEach(currentDriver::addTransactionToHistory);

            // FIXED: Find and set the ONGOING transaction
            for (Transaction t : transactions) {
                if (t.getStatus() == com.src.enumerations.TransactionStatus.ONGOING) {
                    currentDriver.setCurrentTransaction(t);
                    break;
                }
            }

            try {
                viewHandler.showDriverMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid login");
        }
    }

    @FXML
    void onBackClicked(ActionEvent event) throws IOException {
        viewHandler.showAuthView();
    }

    @FXML
    void onLogoutClicked(ActionEvent event) throws IOException {
        currentDriver = null; // Log out
        viewHandler.showAuthView();
    }

    @FXML
    void onViewRideClicked(ActionEvent event) throws IOException {
        viewHandler.showDriverRideView();
    }

    @FXML
    void onDropoffClicked(ActionEvent event) {
        Transaction ride = currentDriver.getCurrentTransaction();
        if (ride != null) {
            ride.setStatus(com.src.enumerations.TransactionStatus.COMPLETED);
            transactionDAO.updateTransaction(ride);

            currentDriver.addEarning(ride.getCost());
            currentDriver.setAvailable(true);
            driverDAO.updateDriver(currentDriver);
            driverDAO.updateDriverBalance(currentDriver); // ADDED: Save balance

            currentDriver.removeCurrentTransaction();

            System.out.println("Ride completed. Earnings added.");
            populateRideDetails(); // Refresh view
        }
    }

    // --- ADDED: Handlers for Menu Buttons ---

    @FXML
    void onManageAccountClicked(ActionEvent event) throws IOException {
        viewHandler.showDriverManageAccount();
    }

    @FXML
    void onChangePasswordClicked(ActionEvent event) throws IOException {
        viewHandler.showDriverChangePassword();
    }

    @FXML
    void onViewTransactionsClicked(ActionEvent event) throws IOException {
        viewHandler.showDriverTransactions();
    }

    @FXML
    void onViewVehicleClicked(ActionEvent event) throws IOException {
        viewHandler.showDriverVehicle();
    }

    @FXML
    void onViewReportsClicked(ActionEvent event) throws IOException {
        viewHandler.showDriverReports();
    }

    // --- ADDED: Handlers for sub-pages ---

    @FXML
    void onSaveClicked(ActionEvent event) {
        System.out.println("Saving account...");
        // TODO: Get text from nameField, emailField, etc. and call driverDAO.updateDriver()
    }

    @FXML
    void onUpdatePasswordClicked(ActionEvent event) {
        System.out.println("Updating password...");
        // TODO: Get passwords, check, and call driver.updatePassword() & driverDAO.updateDriver()
    }

    @FXML
    void onBackToMenuClicked(ActionEvent event) throws IOException {
        viewHandler.showDriverMenu();
    }
}