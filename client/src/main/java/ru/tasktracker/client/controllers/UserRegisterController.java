package ru.tasktracker.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.tasktracker.client.models.UserRegisterModel;

import java.io.IOException;

public class UserRegisterController {
    private static UserRegisterController registerController;
    private UserRegisterModel userRegisterModel;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private TextField fullNameTextField;

    @FXML
    private TextField passwordTextField;

    public static UserRegisterController getRegisterController() {
        return registerController;
    }

    @FXML
    void changeWindow(ActionEvent event) throws IOException {
        userRegisterModel.changeWindow();
    }

    @FXML
    void register(ActionEvent event) throws Exception {
        userRegisterModel.register();
    }

    @FXML
    void initialize() {
        registerController = this;
        userRegisterModel = new UserRegisterModel();
    }

    public TextField getEmailTextField() {
        return emailTextField;
    }

    public Label getErrorMessageLabel() {
        return errorMessageLabel;
    }

    public TextField getFullNameTextField() {
        return fullNameTextField;
    }

    public TextField getPasswordTextField() {
        return passwordTextField;
    }
}
