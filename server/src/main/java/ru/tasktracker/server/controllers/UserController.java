package ru.tasktracker.server.controllers;

import com.sun.net.httpserver.HttpExchange;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tasktracker.server.bodies.requests.UserReqBody;
import ru.tasktracker.server.bodies.responses.MultiuserResBody;
import ru.tasktracker.server.bodies.responses.UserResBody;
import ru.tasktracker.server.entities.Role;
import ru.tasktracker.server.entities.Task;
import ru.tasktracker.server.entities.User;
import ru.tasktracker.server.utilities.HibernateUtility;
import ru.tasktracker.server.utilities.HttpUtility;
import ru.tasktracker.server.utilities.JwtUtility;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public class UserController {
    private final HttpExchange exchange;
    private final PasswordEncoder passwordEncoder;
    private final SessionFactory sessionFactory;

    public UserController(HttpExchange exchange) {
        this.exchange = exchange;

        passwordEncoder = new BCryptPasswordEncoder();
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

            List<User> users = session.createQuery("from User", User.class)
                    .getResultList();

            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_OK, new MultiuserResBody(true, users));

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_CONFLICT, new MultiuserResBody(false, e.getMessage()));
        } finally {
            session.close();
        }
    }

    public void register() throws IOException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            UserReqBody userReqBody = HttpUtility.getReqBody(exchange, UserReqBody.class);
            if (userReqBody.getFullName().equals("") || userReqBody.getEmail().equals("") || userReqBody.getPassword().equals("")) {
                throw new Exception("Fields cannot be empty");
            }

            User user = session.createQuery("from User where email = :email", User.class)
                    .setParameter("email", userReqBody.getEmail())
                    .getSingleResultOrNull();
            if (user != null) {
                throw new Exception("User with this email already exists");
            }

            List<User> users = session.createQuery("from User", User.class)
                    .getResultList();

            user = new User();
            user.setRole(session.get(Role.class, users.isEmpty() ? 1 : 2));
            user.setFullName(userReqBody.getFullName());
            user.setEmail(userReqBody.getEmail());
            user.setPassword(passwordEncoder.encode(userReqBody.getPassword()));
            session.save(user);
            users.add(user);

            List<Task> tasks;
            if (user.getRole().getId() == 1) {
                tasks = session.createQuery("from Task order by expirationTime asc", Task.class)
                        .getResultList();
            } else {
                tasks = session.createQuery("from Task where user = :user order by expirationTime asc", Task.class)
                        .setParameter("user", user)
                        .getResultList();
            }

            String jwt = JwtUtility.generate(user.getId());

            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_CREATED, new UserResBody(true, jwt, user, users, tasks));

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_CONFLICT, new UserResBody(false, e.getMessage()));
        } finally {
            session.close();
        }
    }

    public void login() throws IOException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            UserReqBody userReqBody = HttpUtility.getReqBody(exchange, UserReqBody.class);
            if (userReqBody.getEmail().equals("") || userReqBody.getPassword().equals("")) {
                throw new Exception("Fields cannot be empty");
            }

            User user = session.createQuery("from User where email = :email", User.class)
                    .setParameter("email", userReqBody.getEmail())
                    .getSingleResultOrNull();
            if (user == null) {
                throw new Exception("User with this email does not exist");
            } else if (!passwordEncoder.matches(userReqBody.getPassword(), user.getPassword())) {
                throw new Exception("Invalid password");
            }

            List<User> users = session.createQuery("from User", User.class)
                    .getResultList();

            List<Task> tasks;
            if (user.getRole().getId() == 1) {
                tasks = session.createQuery("from Task order by expirationTime asc", Task.class)
                        .getResultList();
            } else {
                tasks = session.createQuery("from Task where user = :user order by expirationTime asc", Task.class)
                        .setParameter("user", user)
                        .getResultList();
            }

            String jwt = JwtUtility.generate(user.getId());

            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_ACCEPTED, new UserResBody(true, jwt, user, users, tasks));

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_CONFLICT, new UserResBody(false, e.getMessage()));
        } finally {
            session.close();
        }
    }

    public void privilege() throws IOException {
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

            userId = HttpUtility.getIdParameter(exchange);
            if (userId == null) {
                throw new Exception("Invalid request");
            } else if (session.get(User.class, userId) == null) {
                throw new Exception("User with this id does not exist");
            }

            User user = session.get(User.class, userId);
            user.setRole(session.get(Role.class, 1));
            session.update(user);

            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_ACCEPTED, new UserResBody(true, "Privilege granted"));

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_CONFLICT, new UserResBody(false, e.getMessage()));
        } finally {
            session.close();
        }
    }
}
