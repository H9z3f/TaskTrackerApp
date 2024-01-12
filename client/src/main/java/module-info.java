module ru.tasktracker.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;


    exports ru.tasktracker.client;
    opens ru.tasktracker.client to javafx.fxml;
    exports ru.tasktracker.client.controllers;
    opens ru.tasktracker.client.controllers to javafx.fxml;
    exports ru.tasktracker.client.dtos;
    opens ru.tasktracker.client.dtos to com.fasterxml.jackson.databind;
    exports ru.tasktracker.client.bodies.requests;
    opens ru.tasktracker.client.bodies.requests to com.fasterxml.jackson.databind;
    exports ru.tasktracker.client.bodies.responses;
    opens ru.tasktracker.client.bodies.responses to com.fasterxml.jackson.databind;
}