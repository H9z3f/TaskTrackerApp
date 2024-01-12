package ru.tasktracker.client.states;

import javafx.stage.Stage;
import ru.tasktracker.client.bodies.responses.UserResBody;

public class ApplicationState {
    private static Stage stage;
    private static UserResBody userResBody;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ApplicationState.stage = stage;
    }

    public static UserResBody getUserResBody() {
        return userResBody;
    }

    public static void setUserResBody(UserResBody userResBody) {
        ApplicationState.userResBody = userResBody;
    }
}
