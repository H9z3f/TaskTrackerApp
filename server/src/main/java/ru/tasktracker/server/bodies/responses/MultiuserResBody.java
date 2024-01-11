package ru.tasktracker.server.bodies.responses;

import ru.tasktracker.server.entities.User;

import java.util.List;

public class MultiuserResBody {
    private boolean success;
    private String message;
    private List<User> users;

    public MultiuserResBody(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public MultiuserResBody(boolean success, List<User> users) {
        this.success = success;
        this.users = users;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
