package ru.tasktracker.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.tasktracker.client.models.UserLoginModel;

import java.io.IOException;

public class UserLoginController {
    private static UserLoginController loginController;
    private UserLoginModel userLoginModel;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private TextField passwordTextField;

    public static UserLoginController getLoginController() {
        return loginController;
    }

    @FXML
    void changeWindow(ActionEvent event) throws IOException {
        userLoginModel.changeWindow();
    }

    @FXML
    void login(ActionEvent event) throws Exception {
        userLoginModel.login();
    }

    @FXML
    void initialize() {
        loginController = this;
        userLoginModel = new UserLoginModel();
    }

    public TextField getEmailTextField() {
        return emailTextField;
    }

    public Label getErrorMessageLabel() {
        return errorMessageLabel;
    }

    public TextField getPasswordTextField() {
        return passwordTextField;
    }
}
