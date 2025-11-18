package com.src;

import com.src.controllers.AuthController;
import com.src.controllers.DriverController;
import com.src.controllers.PassengerController;
import com.src.database.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Manages scene switching for the application.
 * This class loads FXML files, injects controllers, and sets the stage's scene.
 */
public class ViewHandler {

    private Stage primaryStage;
    private PassengerDAO passengerDAO;
    private DriverDAO driverDAO;
    private VehicleDAO vehicleDAO;
    private TransactionDAO transactionDAO;

    public ViewHandler(Stage primaryStage, PassengerDAO passengerDAO, DriverDAO driverDAO, VehicleDAO vehicleDAO, TransactionDAO transactionDAO) {
        this.primaryStage = primaryStage;
        this.passengerDAO = passengerDAO;
        this.driverDAO = driverDAO;
        this.vehicleDAO = vehicleDAO;
        this.transactionDAO = transactionDAO;
    }

    /**
     * Loads a view, sets its controller, and initializes it.
     */
    private <T> T loadView(String fxmlFile, Class<T> controllerClass) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/src/view/" + fxmlFile));

        Parent root = loader.load();
        T controller = loader.getController();

        // Initialize the controller with necessary dependencies
        if (controller instanceof AuthController) {
            ((AuthController) controller).init(this);
        } else if (controller instanceof PassengerController) {
            ((PassengerController) controller).init(this, passengerDAO, driverDAO, vehicleDAO, transactionDAO);
        } else if (controller instanceof DriverController) {
            ((DriverController) controller).init(this, driverDAO, vehicleDAO, transactionDAO, passengerDAO);
        }

        primaryStage.setScene(new Scene(root));
        return controller;
    }

    // --- Auth Views ---
    public void showAuthView() throws IOException {
        loadView("AuthView.fxml", AuthController.class);
    }

    public void showPassengerLogin() throws IOException {
        loadView("PassengerLogin.fxml", PassengerController.class);
    }

    public void showDriverLogin() throws IOException {
        loadView("DriverLogin.fxml", DriverController.class);
    }

    // --- Signup Views (Added) ---
    public void showPassengerSignup() throws IOException {
        loadView("PassengerSignup.fxml", PassengerController.class);
    }

    public void showDriverSignup() throws IOException {
        loadView("DriverSignup.fxml", DriverController.class);
    }

    // --- Passenger Views ---
    public void showPassengerMenu() throws IOException {
        loadView("PassengerMenu.fxml", PassengerController.class);
    }

    public void showPassengerBookRide() throws IOException {
        loadView("PassengerBookRide.fxml", PassengerController.class);
    }

    public void showPassengerRideView() throws IOException {
        loadView("PassengerViewRide.fxml", PassengerController.class);
    }

    public void showPassengerChangePassword() throws IOException {
        loadView("PassengerChangePassword.fxml", PassengerController.class);
    }

    public void showPassengerManageAccount() throws IOException {
        loadView("PassengerManageAccount.fxml", PassengerController.class);
    }

    public void showPassengerTransactions() throws IOException {
        loadView("PassengerViewTransactions.fxml", PassengerController.class);
    }

    // --- Driver Views ---
    public void showDriverMenu() throws IOException {
        loadView("DriverMenu.fxml", DriverController.class);
    }

    public void showDriverRideView() throws IOException {
        loadView("DriverViewRide.fxml", DriverController.class);
    }

    public void showDriverChangePassword() throws IOException {
        loadView("DriverChangePassword.fxml", DriverController.class);
    }

    public void showDriverManageAccount() throws IOException {
        loadView("DriverManageAccount.fxml", DriverController.class);
    }

    public void showDriverReports() throws IOException {
        loadView("DriverViewReports.fxml", DriverController.class);
    }

    public void showDriverTransactions() throws IOException {
        loadView("DriverViewTransactions.fxml", DriverController.class);
    }

    public void showDriverVehicle() throws IOException {
        loadView("DriverViewVehicle.fxml", DriverController.class);
    }

    public void showPassengerCashIn() throws IOException {
        loadView("PassengerCashIn.fxml", PassengerController.class);
    }

    public void showPassengerReports() throws IOException {
        loadView("PassengerViewReports.fxml", PassengerController.class);
    }
}