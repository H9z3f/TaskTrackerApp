package ru.tasktracker.client.bodies.responses;

import ru.tasktracker.client.dtos.Task;

import java.util.List;

public class MultitaskResBody {
    private boolean success;
    private String message;
    private List<Task> tasks;

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
