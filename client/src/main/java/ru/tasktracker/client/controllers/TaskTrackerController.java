package ru.tasktracker.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.tasktracker.client.dtos.User;
import ru.tasktracker.client.models.TaskTrackerModel;

import java.io.IOException;

public class TaskTrackerController {
    private static TaskTrackerController taskTrackerController;
    private TaskTrackerModel taskTrackerModel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private HBox supervisorPanelVBox;

    @FXML
    private Label systemRoleLabel;

    @FXML
    private VBox taskListVBox;

    @FXML
    private Label tasksAmountLabel;

    @FXML
    private ComboBox<User> userListComboBox;

    public static TaskTrackerController getTaskTrackerController() {
        return taskTrackerController;
    }

    @FXML
    void grantPrivilege(ActionEvent event) throws Exception {
        taskTrackerModel.grantPrivilege();
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        taskTrackerModel.logOut();
    }

    @FXML
    void updateTaskList(ActionEvent event) throws Exception {
        taskTrackerModel.updateTaskList();
    }

    @FXML
    void initialize() {
        taskTrackerController = this;
        taskTrackerModel = new TaskTrackerModel();
        taskTrackerModel.initialize();
    }

    public Label getFullNameLabel() {
        return fullNameLabel;
    }

    public HBox getSupervisorPanelVBox() {
        return supervisorPanelVBox;
    }

    public Label getSystemRoleLabel() {
        return systemRoleLabel;
    }

    public VBox getTaskListVBox() {
        return taskListVBox;
    }

    public Label getTasksAmountLabel() {
        return tasksAmountLabel;
    }

    public ComboBox<User> getUserListComboBox() {
        return userListComboBox;
    }
}
