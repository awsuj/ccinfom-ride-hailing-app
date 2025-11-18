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
import com.src.model.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PassengerController {

    // --- DAOs ---
    private PassengerDAO passengerDAO;
    private DriverDAO driverDAO;
    private VehicleDAO vehicleDAO;
    private TransactionDAO transactionDAO;

    // --- Core ---
    private ViewHandler viewHandler;
    private static Passenger currentPassenger;

    // --- FXML Fields (Existing) ---
    @FXML private Label passengerNameLabel;
    @FXML private Label balanceLabel;
    @FXML private Button logoutButton;
    @FXML private Button changePasswordButton;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button backButton;
    @FXML private TextField ageField;
    @FXML private TextField occupationField;
    @FXML private ComboBox<String> genderCombo;
    @FXML private Button signupButton;
    @FXML private TextField pickupField;
    @FXML private TextField dropoffField;
    @FXML private Button requestRideButton;
    @FXML private Button rideButton;
    @FXML private Button viewTransactionsButton;
    @FXML private Label pickupValue;
    @FXML private Label dropoffValue;
    @FXML private Label driverNameValue;
    @FXML private Label vehicleValue;
    @FXML private Label statusValue;
    @FXML private Button cancelRideButton;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private Button saveButton;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button updatePasswordButton;
    @FXML private TextField cashInAmountField;
    @FXML private Button confirmCashInButton;
    @FXML private Button cashInMenuButton;

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, String> colTime;
    @FXML private TableColumn<Transaction, String> colPickup;
    @FXML private TableColumn<Transaction, String> colDropoff;
    @FXML private TableColumn<Transaction, Double> colPrice;
    @FXML private TableColumn<Transaction, String> colStatus;

    // --- Report Fields (NEW) ---
    @FXML private BarChart<String, Number> expenditureChart;
    @FXML private Label avgExpenditureLabel;
    @FXML private Label avgRidesLabel;
    @FXML private Label completionRateLabel;
    @FXML private Button viewReportsButton;

    @FXML
    public void initialize() {
        if (genderCombo != null) {
            genderCombo.getItems().addAll("Male", "Female", "Other");
        }

        if (transactionsTable != null) {
            colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            colPickup.setCellValueFactory(new PropertyValueFactory<>("pickupLocation"));
            colDropoff.setCellValueFactory(new PropertyValueFactory<>("dropoffLocation"));
            colPrice.setCellValueFactory(new PropertyValueFactory<>("cost"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        }
    }

    public void init(ViewHandler viewHandler, PassengerDAO pDAO, DriverDAO dDAO, VehicleDAO vDAO, TransactionDAO tDAO) {
        this.viewHandler = viewHandler;
        this.passengerDAO = pDAO;
        this.driverDAO = dDAO;
        this.vehicleDAO = vDAO;
        this.transactionDAO = tDAO;

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
            if (transactionsTable != null) {
                transactionsTable.getItems().setAll(currentPassenger.getTransactionHistory());
            }
            if (nameField != null) {
                nameField.setText(currentPassenger.getName());
                phoneField.setText(currentPassenger.getPhoneNumber());
                if (emailField != null) emailField.setText(currentPassenger.getEmail());
            }
            // Generate reports if on the reports page
            if (expenditureChart != null) {
                generateReports();
            }
        }
    }

    /**
     * NEW: Generates statistics for the passenger.
     */
    private void generateReports() {
        List<Transaction> history = currentPassenger.getTransactionHistory();
        if (history == null || history.isEmpty()) {
            return;
        }

        Map<YearMonth, Double> expenditurePerMonth = new HashMap<>();
        Map<YearMonth, Integer> ridesPerMonth = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        int completedRides = 0;
        int totalRides = history.size();

        for (Transaction t : history) {
            if (t.getStatus() == TransactionStatus.COMPLETED) {
                completedRides++;
                try {
                    LocalDate date = LocalDate.parse(t.getTime(), formatter);
                    YearMonth ym = YearMonth.from(date);

                    expenditurePerMonth.put(ym, expenditurePerMonth.getOrDefault(ym, 0.0) + t.getCost());
                    ridesPerMonth.put(ym, ridesPerMonth.getOrDefault(ym, 0) + 1);
                } catch (Exception e) {
                    System.out.println("Error parsing date: " + t.getTime());
                }
            }
        }

        // Calculate Averages
        int uniqueMonths = expenditurePerMonth.size();
        double totalExpenditure = expenditurePerMonth.values().stream().mapToDouble(Double::doubleValue).sum();

        double avgExpenditure = uniqueMonths > 0 ? totalExpenditure / uniqueMonths : 0.0;
        double avgRides = uniqueMonths > 0 ? (double) completedRides / uniqueMonths : 0.0;
        double completionRate = totalRides > 0 ? ((double) completedRides / totalRides) * 100 : 0.0;

        // Update UI
        avgExpenditureLabel.setText(String.format("Average Expenditure/Month: P%.2f", avgExpenditure));
        avgRidesLabel.setText(String.format("Average Rides/Month: %.1f", avgRides));
        completionRateLabel.setText(String.format("Completion Rate: %.1f%%", completionRate));

        // Populate Chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Expenditure");

        expenditurePerMonth.keySet().stream().sorted().forEach(ym -> {
            series.getData().add(new XYChart.Data<>(ym.toString(), expenditurePerMonth.get(ym)));
        });

        expenditureChart.getData().clear();
        expenditureChart.getData().add(series);
    }

    // --- Navigation Handlers ---

    @FXML
    void onViewReportsClicked(ActionEvent event) throws IOException {
        viewHandler.showPassengerReports();
    }

    @FXML
    void onCashInMenuClicked(ActionEvent event) throws IOException {
        viewHandler.showPassengerCashIn();
    }

    // ... (Keep all existing methods: onLoginClicked, onBookRideClicked, etc. EXACTLY as they were) ...

    @FXML
    void onConfirmCashInClicked(ActionEvent event) {
        String amountText = cashInAmountField.getText();
        if (amountText.isEmpty()) { System.out.println("Please enter an amount."); return; }
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) { System.out.println("Amount must be positive."); return; }
            currentPassenger.addBalance(amount);
            passengerDAO.updatePassengerBalance(currentPassenger);
            balanceLabel.setText(String.format("Balance: P%.2f", currentPassenger.getBalance()));
            System.out.println("Cash In Successful!");
            cashInAmountField.clear();
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format.");
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
            currentPassenger.getTransactionHistory().clear();
            transactions.forEach(currentPassenger::addTransactionToHistory);
            for (Transaction t : transactions) {
                if (t.getStatus() == TransactionStatus.ONGOING) {
                    currentPassenger.setCurrentTransaction(t);
                    break;
                }
            }
            try { viewHandler.showPassengerMenu(); } catch (IOException e) { e.printStackTrace(); }
        } else {
            System.out.println("Invalid login");
        }
    }

    @FXML
    void onBookRideClicked(ActionEvent event) {
        try {
            viewHandler.showPassengerBookRide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onRequestRideClicked(ActionEvent event) {
        if (currentPassenger.getCurrentTransaction() != null) {
            System.out.println("You already have an ongoing ride!");
            try { viewHandler.showPassengerRideView(); } catch (IOException e) { e.printStackTrace(); }
            return;
        }
        String pickupLocation = pickupField.getText();
        String dropoffLocation = dropoffField.getText();
        if (pickupLocation.isEmpty() || dropoffLocation.isEmpty()) { System.out.println("Please enter locations."); return; }

        List<Driver> drivers = driverDAO.getAllDrivers();
        List<Driver> availableDrivers = new ArrayList<>();
        for (Driver d : drivers) if (d.isAvailable()) availableDrivers.add(d);
        if (availableDrivers.isEmpty()) { System.out.println("No available drivers."); return; }

        Driver selectedDriver = availableDrivers.get(new Random().nextInt(availableDrivers.size()));

        if (selectedDriver.getVehicles() == null || selectedDriver.getVehicles().isEmpty()) {
            selectedDriver.setVehicles((ArrayList<Vehicle>) vehicleDAO.findAllByDriverID(selectedDriver.getDriverID()));
        }
        if (selectedDriver.getCurrentVehicle() == null && !selectedDriver.getVehicles().isEmpty()) {
            selectedDriver.setCurrentVehicle(selectedDriver.getVehicles().get(0));
        }
        if (selectedDriver.getCurrentVehicle() == null) { System.out.println("Selected driver has no vehicle."); return; }

        double cost = 80 + new Random().nextDouble() * (500 - 80);
        if (currentPassenger.getBalance() < cost) { System.out.println("Insufficient balance."); return; }

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Transaction newTransaction = new Transaction(0, currentPassenger.getPassengerID(), selectedDriver.getCurrentVehicle().getVehicleID(), pickupLocation, dropoffLocation, cost, time);

        currentPassenger.setCurrentTransaction(newTransaction);
        currentPassenger.addTransactionToHistory(newTransaction);
        currentPassenger.deductBalance(cost);
        selectedDriver.setCurrentTransaction(newTransaction);
        selectedDriver.setAvailable(false);

        transactionDAO.addTransaction(newTransaction);
        passengerDAO.updatePassengerBalance(currentPassenger);
        driverDAO.updateDriver(selectedDriver);
        System.out.println("Ride booked successfully.");
        try { viewHandler.showPassengerRideView(); } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    void onCancelRideClicked(ActionEvent event) {
        Transaction ride = currentPassenger.getCurrentTransaction();
        if (ride != null) {
            ride.setStatus(TransactionStatus.CANCELLED);
            currentPassenger.addBalance(ride.getCost());
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
            passengerDAO.updatePassengerBalance(currentPassenger);
            System.out.println("Ride cancelled.");
            populateRideDetails();
        }
    }

    private void populateRideDetails() {
        Transaction ride = currentPassenger.getCurrentTransaction();
        if (ride != null) {
            Vehicle vehicle = vehicleDAO.findByID(ride.getVehicleID());
            Driver driver = (vehicle != null) ? driverDAO.findByID(vehicle.getDriverID()) : null;
            pickupValue.setText(ride.getPickupLocation());
            dropoffValue.setText(ride.getDropoffLocation());
            driverNameValue.setText(driver != null ? driver.getName() : "N/A");
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

    // --- Standard Handlers ---
    @FXML void onBackClicked(ActionEvent event) throws IOException { viewHandler.showAuthView(); }
    @FXML void onLogoutClicked(ActionEvent event) throws IOException { currentPassenger = null; viewHandler.showAuthView(); }
    @FXML void onManageAccountClicked(ActionEvent event) throws IOException { viewHandler.showPassengerManageAccount(); }
    @FXML void onChangePasswordClicked(ActionEvent event) throws IOException { viewHandler.showPassengerChangePassword(); }
    @FXML void onViewTransactionsClicked(ActionEvent event) throws IOException { viewHandler.showPassengerTransactions(); }
    @FXML void onBackToMenuClicked(ActionEvent event) throws IOException { viewHandler.showPassengerMenu(); }
    @FXML void onGoToSignupClicked(ActionEvent event) throws IOException { viewHandler.showPassengerSignup(); }
    @FXML void onSignupClicked(ActionEvent event) {
        try {
            int age = Integer.parseInt(ageField.getText());
            Passenger newPassenger = new Passenger(0, nameField.getText(), emailField.getText(), passwordField.getText(), phoneField.getText());
            newPassenger.setAge(age);
            newPassenger.setGender(genderCombo.getValue());
            newPassenger.setOccupation(occupationField.getText());
            passengerDAO.addPassenger(newPassenger);
            System.out.println("Passenger account created successfully!");
            viewHandler.showPassengerLogin();
        } catch (Exception e) { e.printStackTrace(); }
    }
    @FXML void onSaveClicked(ActionEvent event) {
        currentPassenger.setName(nameField.getText());
        currentPassenger.setPhoneNumber(phoneField.getText());
        currentPassenger.setEmail(emailField.getText());
        passengerDAO.updatePassenger(currentPassenger);
        System.out.println("Account updated.");
    }
    @FXML void onUpdatePasswordClicked(ActionEvent event) {
        if (currentPassenger == null) return;
        String curP = currentPasswordField.getText();
        String newP = newPasswordField.getText();
        String conP = confirmPasswordField.getText();
        if (currentPassenger.checkPassword(curP)) {
            if (newP.equals(conP) && !newP.isEmpty()) {
                currentPassenger.updatePassword(newP);
                passengerDAO.updatePassenger(currentPassenger);
                System.out.println("Password updated.");
                currentPasswordField.clear(); newPasswordField.clear(); confirmPasswordField.clear();
            } else {
                System.out.println("New passwords do not match.");
            }
        } else {
            System.out.println("Wrong current password.");
        }
    }
}