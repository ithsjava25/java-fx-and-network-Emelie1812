package com.example;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {


        private final String hostName;
        private final HttpClient http = HttpClient.newHttpClient();

        public HelloModel() {
            Dotenv dotenv = Dotenv.load();
            hostName = Objects.requireNonNull(dotenv.get("HOST_NAME"));
        }

    /**
     * Returns a greeting based on the current Java and JavaFX versions.
     */
    public String getGreeting() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        return "Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".";
    }

    public void sendMessage() {

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("Hello World"))
                .uri(URI.create(hostName + "/mytopic"))
                .build();
        try {
            var response = http.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        }
        catch (IOException e) {
            System.out.println("Error sending message");
        }
        catch (InterruptedException e) {
            System.out.println("Interrupted sending message");
        }
    }
}
