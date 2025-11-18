package com.src.controllers;

import com.src.ViewHandler;
import com.src.database.DriverDAO;
import com.src.database.PassengerDAO;
import com.src.database.TransactionDAO;
import com.src.database.VehicleDAO;
import com.src.enumerations.TransactionStatus;
import com.src.model.Driver;
import com.src.model.Transaction;
import com.src.model.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart; // ADDED
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth; // ADDED
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverController {

    private DriverDAO driverDAO;
    private VehicleDAO vehicleDAO;
    private TransactionDAO transactionDAO;
    private PassengerDAO passengerDAO;
    private ViewHandler viewHandler;
    private static Driver currentDriver;

    @FXML private Label driverNameLabel;
    @FXML private Label balanceLabel;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> genderCombo;
    @FXML private Label pickupValue;
    @FXML private Label dropoffValue;
    @FXML private Label passengerNameValue;
    @FXML private Label fareValue;
    @FXML private Label statusValue;
    @FXML private Button dropoffButton;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField licenseField;

    @FXML private Label plateValue;
    @FXML private Label modelValue;
    @FXML private Label brandValue;
    @FXML private Label yearValue;
    @FXML private Label fuelValue;

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, String> colTime;
    @FXML private TableColumn<Transaction, String> colPickup;
    @FXML private TableColumn<Transaction, String> colDropoff;
    @FXML private TableColumn<Transaction, Double> colPrice;
    @FXML private TableColumn<Transaction, String> colStatus;

    // --- Report Fields (NEW) ---
    @FXML private BarChart<String, Number> incomeChart; // Updated generic type
    @FXML private Label avgIncomeLabel;
    @FXML private Label avgRidesLabel;
    @FXML private Label completionRateLabel;

    @FXML
    public void initialize() {
        if (genderCombo != null) genderCombo.getItems().addAll("Male", "Female", "Other");
        if (transactionsTable != null) {
            colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            colPickup.setCellValueFactory(new PropertyValueFactory<>("pickupLocation"));
            colDropoff.setCellValueFactory(new PropertyValueFactory<>("dropoffLocation"));
            colPrice.setCellValueFactory(new PropertyValueFactory<>("cost"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        }
    }

    public void init(ViewHandler viewHandler, DriverDAO driverDAO, VehicleDAO vehicleDAO, TransactionDAO transactionDAO, PassengerDAO passengerDAO) {
        this.viewHandler = viewHandler;
        this.driverDAO = driverDAO;
        this.vehicleDAO = vehicleDAO;
        this.transactionDAO = transactionDAO;
        this.passengerDAO = passengerDAO;

        if (currentDriver != null) {
            if (driverNameLabel != null) driverNameLabel.setText("Driver: " + currentDriver.getName());
            if (balanceLabel != null) balanceLabel.setText(String.format("Earnings: P%.2f", currentDriver.getTotalEarnings()));
            if (pickupValue != null) populateRideDetails();
            if (transactionsTable != null) transactionsTable.getItems().setAll(currentDriver.getTransactionHistory());
            if (plateValue != null) populateVehicleDetails();
            if (nameField != null) populateAccountDetails();
            // Generate reports if on the reports page
            if (incomeChart != null) generateReports();
        }
    }

    /**
     * Calculates metrics and populates the report view.
     */
    private void generateReports() {
        List<Transaction> history = currentDriver.getTransactionHistory();
        if (history == null || history.isEmpty()) {
            return;
        }

        // Maps for aggregation
        Map<YearMonth, Double> incomePerMonth = new HashMap<>();
        Map<YearMonth, Integer> ridesPerMonth = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        int completedRides = 0;
        int totalRides = history.size();

        for (Transaction t : history) {
            // Only count completed rides for Income and Avg Rides
            if (t.getStatus() == TransactionStatus.COMPLETED) {
                completedRides++;
                try {
                    // Parse the date string to get Month/Year
                    LocalDate date = LocalDate.parse(t.getTime(), formatter);
                    YearMonth ym = YearMonth.from(date);

                    incomePerMonth.put(ym, incomePerMonth.getOrDefault(ym, 0.0) + t.getCost());
                    ridesPerMonth.put(ym, ridesPerMonth.getOrDefault(ym, 0) + 1);
                } catch (Exception e) {
                    System.out.println("Error parsing date for report: " + t.getTime());
                }
            }
        }

        // 1. Calculate Averages
        int uniqueMonths = incomePerMonth.size();
        double totalIncome = incomePerMonth.values().stream().mapToDouble(Double::doubleValue).sum();

        double avgIncome = uniqueMonths > 0 ? totalIncome / uniqueMonths : 0.0;
        double avgRides = uniqueMonths > 0 ? (double) completedRides / uniqueMonths : 0.0;

        // 2. Calculate Completion Rate (Completed / Total)
        double completionRate = totalRides > 0 ? ((double) completedRides / totalRides) * 100 : 0.0;

        // 3. Update Labels
        avgIncomeLabel.setText(String.format("Average Income/Month: P%.2f", avgIncome));
        avgRidesLabel.setText(String.format("Average Rides/Month: %.1f", avgRides));
        completionRateLabel.setText(String.format("Completion Rate: %.1f%%", completionRate));

        // 4. Populate Chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Income");

        // Sort months chronologically
        incomePerMonth.keySet().stream().sorted().forEach(ym -> {
            series.getData().add(new XYChart.Data<>(ym.toString(), incomePerMonth.get(ym)));
        });

        incomeChart.getData().clear();
        incomeChart.getData().add(series);
    }

    // ... (Rest of the existing methods: populateRideDetails, onLoginClicked, etc. remain exactly the same) ...

    private void populateRideDetails() {
        Transaction ride = currentDriver.getCurrentTransaction();
        if (ride != null) {
            pickupValue.setText(ride.getPickupLocation());
            dropoffValue.setText(ride.getDropoffLocation());
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
            if (dropoffButton != null) dropoffButton.setDisable(true);
        }
    }

    private void populateVehicleDetails() {
        Vehicle v = currentDriver.getCurrentVehicle();
        if (v != null) {
            plateValue.setText(v.getPlateNumber());
            modelValue.setText(v.getModelName());
            brandValue.setText(v.getCarBrand());
            yearValue.setText(String.valueOf(v.getYearAcquired()));
            fuelValue.setText(v.getFuelType());
        } else {
            plateValue.setText("No Vehicle Assigned");
        }
    }

    private void populateAccountDetails() {
        nameField.setText(currentDriver.getName());
        phoneField.setText(currentDriver.getPhoneNumber());
        if (emailField != null) emailField.setText(currentDriver.getEmail());
    }

    @FXML
    void onLoginClicked(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        Driver d = driverDAO.findByEmail(email);
        if (d != null && d.checkPassword(password)) {
            currentDriver = d;
            currentDriver.setVehicles((ArrayList<Vehicle>) vehicleDAO.findAllByDriverID(d.getDriverID()));
            if (!currentDriver.getVehicles().isEmpty()) currentDriver.setCurrentVehicle(currentDriver.getVehicles().get(0));
            List<Transaction> transactions = transactionDAO.findByDriver(d.getDriverID());
            currentDriver.getTransactionHistory().clear();
            transactions.forEach(currentDriver::addTransactionToHistory);
            for (Transaction t : transactions) {
                if (t.getStatus() == com.src.enumerations.TransactionStatus.ONGOING) {
                    currentDriver.setCurrentTransaction(t);
                    break;
                }
            }
            try { viewHandler.showDriverMenu(); } catch (IOException e) { e.printStackTrace(); }
        } else {
            System.out.println("Invalid login");
        }
    }

    @FXML
    void onSignupClicked(ActionEvent event) {
        try {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String phone = phoneField.getText();
            String ageText = ageField.getText();
            String gender = genderCombo.getValue();
            String license = licenseField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || ageText.isEmpty() || gender == null || license.isEmpty()) {
                System.out.println("Please fill in all fields.");
                return;
            }

            int age = Integer.parseInt(ageText);
            Driver newDriver = new Driver(0, name, email, password, phone);
            newDriver.setAge(age);
            newDriver.setGender(gender);
            newDriver.setLicenseNum(license);

            driverDAO.addDriver(newDriver);
            System.out.println("Driver account created!");
            viewHandler.showDriverLogin();

        } catch (NumberFormatException e) {
            System.out.println("Invalid age format.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSaveClicked(ActionEvent event) {
        if (currentDriver == null) return;
        currentDriver.setName(nameField.getText());
        currentDriver.setPhoneNumber(phoneField.getText());
        currentDriver.setEmail(emailField.getText());
        driverDAO.updateDriver(currentDriver);
        System.out.println("Account updated.");
    }

    @FXML
    void onUpdatePasswordClicked(ActionEvent event) {
        if (currentDriver == null) return;
        String curP = currentPasswordField.getText();
        String newP = newPasswordField.getText();
        String conP = confirmPasswordField.getText();
        if (currentDriver.checkPassword(curP)) {
            if (newP.equals(conP) && !newP.isEmpty()) {
                currentDriver.updatePassword(newP);
                driverDAO.updateDriver(currentDriver);
                System.out.println("Password updated.");
                currentPasswordField.clear(); newPasswordField.clear(); confirmPasswordField.clear();
            } else {
                System.out.println("New passwords do not match.");
            }
        } else {
            System.out.println("Wrong current password.");
        }
    }

    @FXML void onDropoffClicked(ActionEvent event) {
        Transaction ride = currentDriver.getCurrentTransaction();
        if (ride != null) {
            ride.setStatus(com.src.enumerations.TransactionStatus.COMPLETED);
            transactionDAO.updateTransaction(ride);
            currentDriver.addEarning(ride.getCost());
            currentDriver.setAvailable(true);
            driverDAO.updateDriver(currentDriver);
            driverDAO.updateDriverBalance(currentDriver);
            currentDriver.removeCurrentTransaction();
            populateRideDetails();
        }
    }

    @FXML void onGoToSignupClicked(ActionEvent event) throws IOException { viewHandler.showDriverSignup(); }
    @FXML void onBackClicked(ActionEvent event) throws IOException { viewHandler.showAuthView(); }
    @FXML void onLogoutClicked(ActionEvent event) throws IOException { currentDriver = null; viewHandler.showAuthView(); }
    @FXML void onViewRideClicked(ActionEvent event) throws IOException { viewHandler.showDriverRideView(); }
    @FXML void onManageAccountClicked(ActionEvent event) throws IOException { viewHandler.showDriverManageAccount(); }
    @FXML void onChangePasswordClicked(ActionEvent event) throws IOException { viewHandler.showDriverChangePassword(); }
    @FXML void onViewTransactionsClicked(ActionEvent event) throws IOException { viewHandler.showDriverTransactions(); }
    @FXML void onViewVehicleClicked(ActionEvent event) throws IOException { viewHandler.showDriverVehicle(); }
    @FXML void onViewReportsClicked(ActionEvent event) throws IOException { viewHandler.showDriverReports(); }
    @FXML void onBackToMenuClicked(ActionEvent event) throws IOException { viewHandler.showDriverMenu(); }
}