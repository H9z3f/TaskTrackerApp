package ru.tasktracker.server.routers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.tasktracker.server.bodies.responses.TaskResBody;
import ru.tasktracker.server.controllers.UserController;
import ru.tasktracker.server.utilities.HttpUtility;

import java.io.IOException;
import java.net.HttpURLConnection;

public class TaskRouter implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        if (requestMethod.equalsIgnoreCase("GET") && path.equalsIgnoreCase("/task")) {
            new UserController(exchange).getAll();
        } else if (requestMethod.equalsIgnoreCase("POST") && path.equalsIgnoreCase("/task/create")) {
            new UserController(exchange).register();
        } else if (requestMethod.equalsIgnoreCase("POST") && path.equalsIgnoreCase("/task/edit")) {
            new UserController(exchange).login();
        } else {
            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, new TaskResBody(false, "Invalid request"));
        }
    }
}
