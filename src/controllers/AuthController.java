package com.src.controllers;

import com.src.ViewHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class AuthController {

    @FXML
    private Button driverLoginButton;

    @FXML
    private Button passengerLoginButton;

    private ViewHandler viewHandler;

    /**
     * Initialize controller with the ViewHandler.
     */
    public void init(ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
    }

    @FXML
    void onDriverLoginClicked(ActionEvent event) {
        try {
            viewHandler.showDriverLogin();
        } catch (IOException e) {
            e.printStackTrace();
            // Show error alert
        }
    }

    @FXML
    void onPassengerLoginClicked(ActionEvent event) {
        try {
            viewHandler.showPassengerLogin();
        } catch (IOException e) {
            e.printStackTrace();
            // Show error alert
        }
    }
}