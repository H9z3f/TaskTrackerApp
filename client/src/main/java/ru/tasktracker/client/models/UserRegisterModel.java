package ru.tasktracker.client.models;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.tasktracker.client.App;
import ru.tasktracker.client.bodies.requests.UserReqBody;
import ru.tasktracker.client.bodies.responses.UserResBody;
import ru.tasktracker.client.controllers.UserRegisterController;
import ru.tasktracker.client.states.ApplicationState;
import ru.tasktracker.client.utilities.HttpUtility;

import java.io.IOException;

public class UserRegisterModel {
    private final UserRegisterController registerController;
    private final Stage stage;

    public UserRegisterModel() {
        registerController = UserRegisterController.getRegisterController();
        stage = ApplicationState.getStage();
    }

    public void changeWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login window");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void register() throws Exception {
        String route = "/user/register";
        String fullName = registerController.getFullNameTextField().getText();
        String email = registerController.getEmailTextField().getText();
        String password = registerController.getPasswordTextField().getText();

        UserResBody userResBody = HttpUtility.sendPostRequest(null, route, new UserReqBody(fullName, email, password), UserResBody.class);
        if (!userResBody.isSuccess()) {
            registerController.getErrorMessageLabel().setText(userResBody.getMessage());
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
