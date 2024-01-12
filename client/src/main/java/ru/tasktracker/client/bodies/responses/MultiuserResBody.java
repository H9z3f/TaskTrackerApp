package ru.tasktracker.client.bodies.responses;

import ru.tasktracker.client.dtos.User;

import java.util.List;

public class MultiuserResBody {
    private boolean success;
    private String message;
    private List<User> users;

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
