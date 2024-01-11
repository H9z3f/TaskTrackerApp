package ru.tasktracker.server;

import com.sun.net.httpserver.HttpServer;
import ru.tasktracker.server.routers.TaskRouter;
import ru.tasktracker.server.routers.UserRouter;
import ru.tasktracker.server.utilities.SystemUtility;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private final int PORT;
    private final int THREADS;
    private HttpServer httpServer;
    private String message;

    public Server() {
        PORT = Integer.parseInt(SystemUtility.getEnvironmentVariableOrDefault("PORT", "8080"));
        THREADS = Integer.parseInt(SystemUtility.getEnvironmentVariableOrDefault("THREADS", "0"));

        configure();
    }

    private void configure() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), THREADS);
            httpServer.setExecutor(null);
            httpServer.createContext("/user", new UserRouter());
            httpServer.createContext("/task", new TaskRouter());

            start();

            message = "Server is listening on port " + PORT;
        } catch (IOException e) {
            message = e.getMessage();
        } finally {
            System.out.println(message);
        }
    }

    private void start() {
        httpServer.start();
    }
}
