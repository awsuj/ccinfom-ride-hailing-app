package com.src;

import com.src.database.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class.
 * This class initializes the application, DAOs, and the ViewHandler.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // --- Initialize DAOs ---
        PassengerDAO passengerDAO = new PassengerDAO();
        DriverDAO driverDAO = new DriverDAO();
        VehicleDAO vehicleDAO = new VehicleDAO();
        TransactionDAO transactionDAO = new TransactionDAO();

        // --- Initialize ViewHandler ---
        // The ViewHandler will manage loading FXML and switching scenes.
        ViewHandler viewHandler = new ViewHandler(primaryStage, passengerDAO, driverDAO, vehicleDAO, transactionDAO);

        // --- Show the initial view ---
        viewHandler.showAuthView();

        primaryStage.setTitle("Ride-Hailing App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}