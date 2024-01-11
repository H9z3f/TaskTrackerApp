package ru.tasktracker.server.bodies.responses;

import ru.tasktracker.server.entities.Task;
import ru.tasktracker.server.entities.User;

import java.util.List;

public class UserResBody {
    private boolean success;
    private String message;
    private String jwt;
    private User user;
    private List<User> users;
    private List<Task> tasks;

    public UserResBody(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public UserResBody(boolean success, String jwt, User user, List<User> users, List<Task> tasks) {
        this.success = success;
        this.jwt = jwt;
        this.user = user;
        this.users = users;
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

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
