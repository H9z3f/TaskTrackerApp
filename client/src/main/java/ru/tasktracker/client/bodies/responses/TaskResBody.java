package ru.tasktracker.client.bodies.responses;

import ru.tasktracker.client.dtos.Task;

public class TaskResBody {
    private boolean success;
    private String message;
    private Task task;

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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
