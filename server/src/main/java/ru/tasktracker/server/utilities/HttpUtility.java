package ru.tasktracker.server.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.springframework.http.HttpHeaders;
import ru.tasktracker.server.bodies.requests.TaskReqBody;
import ru.tasktracker.server.bodies.requests.UserReqBody;
import ru.tasktracker.server.bodies.responses.MultitaskResBody;
import ru.tasktracker.server.bodies.responses.MultiuserResBody;
import ru.tasktracker.server.bodies.responses.TaskResBody;
import ru.tasktracker.server.bodies.responses.UserResBody;

import java.io.IOException;

public class HttpUtility {
    private static ObjectMapper objectMapper;

    public static void setObjectMapper() {
        objectMapper = new ObjectMapper();
    }

    public static String getAuthHeader(HttpExchange exchange) {
        String authHeader = exchange.getRequestHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.substring("Bearer ".length());
    }

    public static Long getIdParameter(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (pathParts.length != 4 || !pathParts[pathParts.length - 1].matches("\\d+")) {
            return null;
        }

        return Long.valueOf(pathParts[pathParts.length - 1]);
    }

    public static UserReqBody getUserReqBody(HttpExchange exchange) throws IOException {
        if (objectMapper == null) {
            setObjectMapper();
        }

        return objectMapper.readValue(exchange.getRequestBody(), UserReqBody.class);
    }

    public static TaskReqBody getTaskReqBody(HttpExchange exchange) throws IOException {
        if (objectMapper == null) {
            setObjectMapper();
        }

        return objectMapper.readValue(exchange.getRequestBody(), TaskReqBody.class);
    }

    public static void sendResponse(HttpExchange exchange, int code, UserResBody resBody) throws IOException {
        if (objectMapper == null) {
            setObjectMapper();
        }

        exchange.sendResponseHeaders(code, 0);
        objectMapper.writeValue(exchange.getResponseBody(), resBody);
    }

    public static void sendResponse(HttpExchange exchange, int code, MultiuserResBody resBody) throws IOException {
        if (objectMapper == null) {
            setObjectMapper();
        }

        exchange.sendResponseHeaders(code, 0);
        objectMapper.writeValue(exchange.getResponseBody(), resBody);
    }

    public static void sendResponse(HttpExchange exchange, int code, TaskResBody resBody) throws IOException {
        if (objectMapper == null) {
            setObjectMapper();
        }

        exchange.sendResponseHeaders(code, 0);
        objectMapper.writeValue(exchange.getResponseBody(), resBody);
    }

    public static void sendResponse(HttpExchange exchange, int code, MultitaskResBody resBody) throws IOException {
        if (objectMapper == null) {
            setObjectMapper();
        }

        exchange.sendResponseHeaders(code, 0);
        objectMapper.writeValue(exchange.getResponseBody(), resBody);
    }
}
