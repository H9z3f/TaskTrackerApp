package ru.tasktracker.client.models;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.tasktracker.client.App;
import ru.tasktracker.client.bodies.requests.UserReqBody;
import ru.tasktracker.client.bodies.responses.UserResBody;
import ru.tasktracker.client.controllers.UserLoginController;
import ru.tasktracker.client.states.ApplicationState;
import ru.tasktracker.client.utilities.HttpUtility;

import java.io.IOException;

public class UserLoginModel {
    private final UserLoginController loginController;
    private final Stage stage;

    public UserLoginModel() {
        loginController = UserLoginController.getLoginController();
        stage = ApplicationState.getStage();
    }

    public void changeWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Registration window");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void login() throws Exception {
        String route = "/user/login";
        String email = loginController.getEmailTextField().getText();
        String password = loginController.getPasswordTextField().getText();

        UserResBody userResBody = HttpUtility.sendPostRequest(null, route, new UserReqBody(email, password), UserResBody.class);
        if (!userResBody.isSuccess()) {
            loginController.getErrorMessageLabel().setText(userResBody.getMessage());
            return;
        }

        ApplicationState.setUserResBody(userResBody);

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("tracker-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Task tracker window");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
