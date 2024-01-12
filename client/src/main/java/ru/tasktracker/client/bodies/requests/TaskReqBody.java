package ru.tasktracker.client.bodies.requests;

import java.util.Date;

public class TaskReqBody {
    private String title;
    private String description;
    private Long userId;
    private Long statusId;
    private Date creationTime;
    private Date expirationTime;

    public TaskReqBody(String title, String description, Long userId, Date expirationTime) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.expirationTime = expirationTime;
    }

    public TaskReqBody(String title, String description, Long userId, Long statusId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.statusId = statusId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }
}
