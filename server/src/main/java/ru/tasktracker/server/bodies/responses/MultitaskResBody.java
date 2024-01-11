package ru.tasktracker.server.bodies.responses;

import ru.tasktracker.server.entities.Task;

import java.util.List;

public class MultitaskResBody {
    private boolean success;
    private String message;
    private List<Task> tasks;

    public MultitaskResBody(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public MultitaskResBody(boolean success, List<Task> tasks) {
        this.success = success;
        this.tasks = tasks;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
