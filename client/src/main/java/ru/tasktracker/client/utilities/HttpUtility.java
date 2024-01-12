package ru.tasktracker.client.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtility {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String BASE_URL = "http://localhost:8080";

    public static <T> T sendGetRequest(String jwt, String route, Class<T> dynamicClass) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + route))
                .header("Authorization", "Bearer " + jwt)
                .GET()
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return OBJECT_MAPPER.readValue(response.body(), dynamicClass);
    }

    public static <T> T sendPostRequest(String jwt, String route, Object requestBody, Class<T> dynamicClass) throws Exception {
        String jsonBody = OBJECT_MAPPER.writeValueAsString(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + route))
                .header("Authorization", "Bearer " + jwt)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return OBJECT_MAPPER.readValue(response.body(), dynamicClass);
    }

    public static <T> T sendPutRequest(String jwt, String route, Object requestBody, Class<T> dynamicClass) throws Exception {
        String jsonBody = OBJECT_MAPPER.writeValueAsString(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + route))
                .header("Authorization", "Bearer " + jwt)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return OBJECT_MAPPER.readValue(response.body(), dynamicClass);
    }

    public static <T> T sendDeleteRequest(String jwt, String route, Class<T> dynamicClass) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + route))
                .header("Authorization", "Bearer " + jwt)
                .DELETE()
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return OBJECT_MAPPER.readValue(response.body(), dynamicClass);
    }
}
