package ru.tasktracker.client.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.tasktracker.client.App;
import ru.tasktracker.client.bodies.requests.TaskReqBody;
import ru.tasktracker.client.bodies.responses.MultitaskResBody;
import ru.tasktracker.client.bodies.responses.MultiuserResBody;
import ru.tasktracker.client.bodies.responses.TaskResBody;
import ru.tasktracker.client.bodies.responses.UserResBody;
import ru.tasktracker.client.controllers.TaskTrackerController;
import ru.tasktracker.client.dtos.Task;
import ru.tasktracker.client.dtos.User;
import ru.tasktracker.client.states.ApplicationState;
import ru.tasktracker.client.utilities.HttpUtility;
import ru.tasktracker.client.views.TaskView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TaskTrackerModel {
    private final TaskTrackerController taskTrackerController;
    private final Stage stage;
    private final UserResBody userResBody;
    private final ObjectMapper objectMapper;
    private Stage secondaryStage;

    public TaskTrackerModel() {
        taskTrackerController = TaskTrackerController.getTaskTrackerController();
        stage = ApplicationState.getStage();
        userResBody = ApplicationState.getUserResBody();
        objectMapper = new ObjectMapper();
    }

    public void initialize() {
        taskTrackerController.getSystemRoleLabel().setText("System role: " + userResBody.getUser().getRole().getRoleName());
        taskTrackerController.getFullNameLabel().setText(userResBody.getUser().getFullName());
        taskTrackerController.getTasksAmountLabel().setText("Tasks: " + userResBody.getTasks().size());

        showTaskList();

        if (userResBody.getUser().getRole().getId() != 1) {
            taskTrackerController.getSupervisorPanelVBox().setVisible(false);

            return;
        }

        showUserList();
    }

    public void logOut() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login window");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void grantPrivilege() throws Exception {
        String route = "/user/privilege/" + taskTrackerController.getUserListComboBox().getSelectionModel().getSelectedItem().getId();
        UserResBody resBody = HttpUtility.sendGetRequest(userResBody.getJwt(), route, UserResBody.class);
        if (!resBody.isSuccess()) {
            return;
        }

        route = "/user";
        MultiuserResBody multiuserResBody = HttpUtility.sendGetRequest(userResBody.getJwt(), route, MultiuserResBody.class);
        if (!multiuserResBody.isSuccess()) {
            return;
        }

        userResBody.setUsers(multiuserResBody.getUsers());

        showUserList();
    }

    public void updateTaskList() throws Exception {
        String route = "/task";
        MultitaskResBody multitaskResBody = HttpUtility.sendGetRequest(userResBody.getJwt(), route, MultitaskResBody.class);
        if (!multitaskResBody.isSuccess()) {
            return;
        }

        userResBody.setTasks(multitaskResBody.getTasks());

        showTaskList();
    }

    private void clearTaskList() {
        taskTrackerController.getTaskListVBox().getChildren().clear();

        if (userResBody.getUser().getRole().getId() != 1) {
            return;
        }

        Button createTaskButton = new Button("Create task");
        createTaskButton.setStyle("-fx-font: 14 Arial; -fx-pref-width: 150px;");
        createTaskButton.setOnAction(actionEvent -> {
            createTask();
        });

        taskTrackerController.getTaskListVBox().getChildren().add(createTaskButton);
    }

    private void showTaskList() {
        clearTaskList();

        for (Task task : userResBody.getTasks()) {
            taskTrackerController.getTaskListVBox().getChildren().add(new TaskView(task, this));
        }
    }

    private void clearUserList() {
        taskTrackerController.getUserListComboBox().getItems().clear();
    }

    private void showUserList() {
        clearUserList();

        for (User user : userResBody.getUsers()) {
            if (user.getRole().getId() == 1) {
                continue;
            }

            taskTrackerController.getUserListComboBox().getItems().add(user);
        }
    }

    private void createTask() {
        secondaryStage = new Stage();
        secondaryStage.setResizable(false);
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        secondaryStage.initOwner(stage);
        secondaryStage.setTitle("Task creation window");

        Label topicLabel = new Label("Fill out the form");
        topicLabel.setStyle("-fx-font: 18 Arial; -fx-font-weight: bold; -fx-pref-width: 300px; -fx-alignment: center;");

        Label titleLabel = new Label("Enter title");
        TextField titleTextField = new TextField();
        titleTextField.setStyle("-fx-pref-width: 300px;");

        Label descriptionLabel = new Label("Enter description");
        TextArea descriptionTextArea = new TextArea();
        descriptionTextArea.setStyle("-fx-pref-width: 300px;");

        Label executorLabel = new Label("Choose executor");
        ComboBox<User> executorComboBox = new ComboBox<>();
        executorComboBox.setStyle("-fx-pref-width: 300px;");
        executorComboBox.getSelectionModel().select(userResBody.getUser());
        for (User user : userResBody.getUsers()) {
            executorComboBox.getItems().add(user);
        }

        Label expirationTimeLabel = new Label("Enter expiration time");
        DatePicker expirationTimePicker = new DatePicker();
        expirationTimePicker.setStyle("-fx-pref-width: 300px;");
        expirationTimePicker.setValue(LocalDate.now());

        Label errorMessageLabel = new Label();
        errorMessageLabel.setStyle("-fx-text-fill: red");

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-pref-width: 300px;");
        submitButton.setOnAction(actionEvent -> {
            try {
                String route = "/task/create";
                String title = titleTextField.getText();
                String description = descriptionTextArea.getText();
                Long executor = executorComboBox.getSelectionModel().getSelectedItem().getId();
                Date expirationTime = Date.from(expirationTimePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

                TaskResBody taskResBody = HttpUtility.sendPostRequest(userResBody.getJwt(), route, new TaskReqBody(title, description, executor, expirationTime), TaskResBody.class);
                if (!taskResBody.isSuccess()) {
                    errorMessageLabel.setText(taskResBody.getMessage());

                    return;
                }

                secondaryStage.close();

                updateTaskList();
            } catch (Exception e) {
                errorMessageLabel.setText(e.getMessage());
            }
        });

        VBox dialogVBox = new VBox(topicLabel, titleLabel, titleTextField, descriptionLabel, descriptionTextArea, executorLabel, executorComboBox, expirationTimeLabel, expirationTimePicker, errorMessageLabel, submitButton);
        dialogVBox.setStyle("-fx-font: 14 Arial; -fx-pref-width: 350px; -fx-padding: 25px; -fx-spacing: 10px;");

        Scene dialogScene = new Scene(dialogVBox);

        secondaryStage.setScene(dialogScene);
        secondaryStage.showAndWait();
    }

    public void editTask(Task task) {
        secondaryStage = new Stage();
        secondaryStage.setResizable(false);
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        secondaryStage.initOwner(stage);
        secondaryStage.setTitle("Task creation window");

        Label topicLabel = new Label("Fill out the form");
        topicLabel.setStyle("-fx-font: 18 Arial; -fx-font-weight: bold; -fx-pref-width: 300px; -fx-alignment: center;");

        Label titleLabel = new Label("Enter title");
        TextField titleTextField = new TextField(task.getTitle());
        titleTextField.setStyle("-fx-pref-width: 300px;");

        Label descriptionLabel = new Label("Enter description");
        TextArea descriptionTextArea = new TextArea(task.getDescription());
        descriptionTextArea.setStyle("-fx-pref-width: 300px;");

        Label executorLabel = new Label("Choose executor");
        ComboBox<User> executorComboBox = new ComboBox<>();
        executorComboBox.setStyle("-fx-pref-width: 300px;");
        executorComboBox.getSelectionModel().select(task.getUser());
        for (User user : userResBody.getUsers()) {
            executorComboBox.getItems().add(user);
        }

        Label statusLabel = new Label("Choose status");
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.setStyle("-fx-pref-width: 300px;");
        statusComboBox.getSelectionModel().select(task.getStatus().getStatusName());
        statusComboBox.getItems().addAll("New", "Assigned", "In work", "Redirected", "Closed");

        Label errorMessageLabel = new Label();
        errorMessageLabel.setStyle("-fx-text-fill: red");

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-pref-width: 300px;");
        submitButton.setOnAction(actionEvent -> {
            try {
                String route = "/task/edit/" + task.getId();
                String title = titleTextField.getText();
                String description = descriptionTextArea.getText();
                Long executor = executorComboBox.getSelectionModel().getSelectedItem().getId();
                String statusString = statusComboBox.getSelectionModel().getSelectedItem();
                Long status = switch (statusString) {
                    case "New" -> 1L;
                    case "Assigned" -> 2L;
                    case "In work" -> 3L;
                    case "Redirected" -> 4L;
                    default -> 5L;
                };

                TaskResBody taskResBody = HttpUtility.sendPostRequest(userResBody.getJwt(), route, new TaskReqBody(title, description, executor, status), TaskResBody.class);
                if (!taskResBody.isSuccess()) {
                    errorMessageLabel.setText(taskResBody.getMessage());

                    return;
                }

                secondaryStage.close();

                updateTaskList();
            } catch (Exception e) {
                errorMessageLabel.setText(e.getMessage());
            }
        });

        VBox dialogVBox = new VBox(topicLabel, titleLabel, titleTextField, descriptionLabel, descriptionTextArea, executorLabel, executorComboBox, statusLabel, statusComboBox, errorMessageLabel, submitButton);
        dialogVBox.setStyle("-fx-font: 14 Arial; -fx-pref-width: 350px; -fx-padding: 25px; -fx-spacing: 10px;");

        Scene dialogScene = new Scene(dialogVBox);

        secondaryStage.setScene(dialogScene);
        secondaryStage.showAndWait();
    }
}
