package ru.tasktracker.server.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

public class HttpUtility {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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

    public static <T> T getReqBody(HttpExchange exchange, Class<T> dynamicClass) throws IOException {
        return OBJECT_MAPPER.readValue(exchange.getRequestBody(), dynamicClass);
    }

    public static void sendResponse(HttpExchange exchange, int code, Object resBody) throws IOException {
        exchange.sendResponseHeaders(code, 0);
        OBJECT_MAPPER.writeValue(exchange.getResponseBody(), resBody);
    }
}
