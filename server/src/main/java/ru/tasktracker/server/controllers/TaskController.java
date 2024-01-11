package ru.tasktracker.server.controllers;

import com.sun.net.httpserver.HttpExchange;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.tasktracker.server.bodies.requests.TaskReqBody;
import ru.tasktracker.server.bodies.responses.MultitaskResBody;
import ru.tasktracker.server.bodies.responses.TaskResBody;
import ru.tasktracker.server.entities.Status;
import ru.tasktracker.server.entities.Task;
import ru.tasktracker.server.entities.User;
import ru.tasktracker.server.utilities.HibernateUtility;
import ru.tasktracker.server.utilities.HttpUtility;
import ru.tasktracker.server.utilities.JwtUtility;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.List;

public class TaskController {
    private final HttpExchange exchange;
    private final SessionFactory sessionFactory;

    public TaskController(HttpExchange exchange) {
        this.exchange = exchange;

        sessionFactory = HibernateUtility.getSessionFactory();
    }

    public void getAll() throws IOException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            String jwt = HttpUtility.getAuthHeader(exchange);
            if (jwt == null) {
                throw new Exception("No access key");
            }

            Long userId = JwtUtility.parse(jwt);
            if (userId == null) {
                throw new Exception("Invalid access key");
            }

            List<Task> tasks;
            if (session.get(User.class, userId).getRole().getId() == 1) {
                tasks = session.createQuery("from Task", Task.class)
                        .getResultList();
            } else {
                tasks = session.createQuery("from Task where user.id = :userId", Task.class)
                        .setParameter("userId", userId)
                        .getResultList();
            }

            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_OK, new MultitaskResBody(true, tasks));

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_CONFLICT, new MultitaskResBody(false, e.getMessage()));
        } finally {
            session.close();
        }
    }

    public void create() throws IOException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            String jwt = HttpUtility.getAuthHeader(exchange);
            if (jwt == null) {
                throw new Exception("No access key");
            }

            Long userId = JwtUtility.parse(jwt);
            if (userId == null) {
                throw new Exception("Invalid access key");
            } else if (session.get(User.class, userId).getRole().getId() != 1) {
                throw new Exception("Not enough permissions");
            }

            TaskReqBody taskReqBody = HttpUtility.getTaskReqBody(exchange);
            if (taskReqBody.getTitle().equals("") || taskReqBody.getDescription().equals("")) {
                throw new Exception("Fields cannot be empty");
            }

            User user = session.get(User.class, taskReqBody.getUserId());
            if (user == null) {
                throw new Exception("User with this id does not exist");
            }

            Date creationTime = new Date();
            if (creationTime.after(taskReqBody.getExpirationTime())) {
                throw new Exception("Expiration time cannot be before creation time");
            }

            Task task = new Task();
            task.setTitle(taskReqBody.getTitle());
            task.setDescription(taskReqBody.getDescription());
            task.setUser(user);
            task.setStatus(session.get(Status.class, userId == taskReqBody.getUserId() ? 1 : 2));
            task.setCreationTime(creationTime);
            task.setExpirationTime(taskReqBody.getExpirationTime());
            session.save(task);

            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_OK, new TaskResBody(true, task));

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_CONFLICT, new TaskResBody(false, e.getMessage()));
        } finally {
            session.close();
        }
    }

    public void edit() throws IOException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            String jwt = HttpUtility.getAuthHeader(exchange);
            if (jwt == null) {
                throw new Exception("No access key");
            }

            Long userId = JwtUtility.parse(jwt);
            if (userId == null) {
                throw new Exception("Invalid access key");
            }

            Long taskId = HttpUtility.getIdParameter(exchange);
            if (taskId == null) {
                throw new Exception("Invalid request");
            } else if (session.get(Task.class, taskId) == null) {
                throw new Exception("Task with this id does not exist");
            } else if (session.get(User.class, userId).getRole().getId() != 1 && session.get(User.class, userId).getId() != session.get(Task.class, taskId).getUser().getId()) {
                throw new Exception("Not enough permissions");
            }

            TaskReqBody taskReqBody = HttpUtility.getTaskReqBody(exchange);
            if (taskReqBody.getTitle().equals("") || taskReqBody.getDescription().equals("")) {
                throw new Exception("Fields cannot be empty");
            }

            User user = session.get(User.class, taskReqBody.getUserId());
            if (user == null) {
                throw new Exception("User with this id does not exist");
            }

            Task task = session.get(Task.class, taskId);
            task.setTitle(taskReqBody.getTitle());
            task.setDescription(taskReqBody.getDescription());
            task.setUser(user);
            task.setStatus(session.get(Status.class, taskReqBody.getStatusId()));
            session.update(task);

            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_OK, new TaskResBody(true, task));

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_CONFLICT, new TaskResBody(false, e.getMessage()));
        } finally {
            session.close();
        }
    }
}
