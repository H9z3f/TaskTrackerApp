package ru.tasktracker.client.views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.tasktracker.client.dtos.Task;
import ru.tasktracker.client.models.TaskTrackerModel;

public class TaskView extends VBox {
    private final Task task;
    private final TaskTrackerModel taskTrackerModel;

    public TaskView(Task task, TaskTrackerModel taskTrackerModel) {
        this.task = task;
        this.taskTrackerModel = taskTrackerModel;

        initialize();
    }

    private void initialize() {
        setStyle("-fx-font: 14 Arial; -fx-max-width: 500px; -fx-padding: 10px; -fx-spacing: 5px; -fx-border-width: 2px; -fx-border-color: black;");

        Label idLabel = new Label("#" + task.getId());
        idLabel.setStyle("-fx-font-weight: bold; -fx-underline: true;");

        Label infoLabel = new Label(task.getTitle() + " (" + task.getStatus().getStatusName() + ")");

        HBox headerHBox = new HBox(idLabel, infoLabel);
        headerHBox.setStyle("-fx-spacing: 10px;");

        Label executorLabel = new Label("Executor: " + task.getUser().getFullName() + " - " + task.getUser().getEmail());

        Label creationTimeLabel = new Label("Creation time: " + task.getCreationTime());

        Label expirationTimeLabel = new Label("Expiration time: " + task.getExpirationTime());

        Label descriptionLabel = new Label("Description: " + task.getDescription());
        descriptionLabel.setStyle("-fx-wrap-text: true;");

        Button editTaskButton = new Button("Edit task");
        editTaskButton.setOnAction(actionEvent -> {
            taskTrackerModel.editTask(task);
        });

        getChildren().addAll(headerHBox, executorLabel, creationTimeLabel, expirationTimeLabel, descriptionLabel, editTaskButton);
    }
}
