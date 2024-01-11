package ru.tasktracker.server.bodies.responses;

import ru.tasktracker.server.entities.Task;

public class TaskResBody {
    private boolean success;
    private String message;
    private Task task;

    public TaskResBody(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public TaskResBody(boolean success, Task task) {
        this.success = success;
        this.task = task;
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
