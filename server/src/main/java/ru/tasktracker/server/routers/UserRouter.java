package ru.tasktracker.server.routers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.tasktracker.server.bodies.responses.UserResBody;
import ru.tasktracker.server.controllers.UserController;
import ru.tasktracker.server.utilities.HttpUtility;

import java.io.IOException;
import java.net.HttpURLConnection;

public class UserRouter implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        if (requestMethod.equalsIgnoreCase("GET") && path.equalsIgnoreCase("/user")) {
            new UserController(exchange).getAll();
        } else if (requestMethod.equalsIgnoreCase("POST") && path.equalsIgnoreCase("/user/register")) {
            new UserController(exchange).register();
        } else if (requestMethod.equalsIgnoreCase("POST") && path.equalsIgnoreCase("/user/login")) {
            new UserController(exchange).login();
        } else if (requestMethod.equalsIgnoreCase("GET") && path.equalsIgnoreCase("/user/privilege")) {
            new UserController(exchange).privilege();
        } else {
            HttpUtility.sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, new UserResBody(false, "Invalid request"));
        }
    }
}
