package com.library.server.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/server")
public class ServerController {
    private Process serverProcess;
    private final StringBuilder serverLogs = new StringBuilder();
    
    @Value("${server.port:8081}")
    private String serverPort;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getServerStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", serverProcess != null && serverProcess.isAlive() ? "online" : "offline");
        response.put("logs", serverLogs.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startServer() {
        if (serverProcess != null && serverProcess.isAlive()) {
            return createResponse(false, "Server is already running");
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "server.jar");
            processBuilder.redirectErrorStream(true);
            serverProcess = processBuilder.start();

            CompletableFuture.runAsync(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(serverProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        serverLogs.append(line).append("\n");
                    }
                } catch (Exception e) {
                    serverLogs.append("Error reading server output: ").append(e.getMessage()).append("\n");
                }
            });

            return createResponse(true, "Server started successfully");
        } catch (Exception e) {
            return createResponse(false, "Failed to start server: " + e.getMessage());
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<Map<String, Object>> stopServer() {
        if (serverProcess == null || !serverProcess.isAlive()) {
            return createResponse(false, "Server is not running");
        }

        try {
            serverProcess.destroy();
            serverProcess.waitFor();
            serverLogs.append("Server stopped\n");
            return createResponse(true, "Server stopped successfully");
        } catch (Exception e) {
            return createResponse(false, "Failed to stop server: " + e.getMessage());
        }
    }

    private ResponseEntity<Map<String, Object>> createResponse(boolean success, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
}
