package com.library.rmi.server.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/server")
public class ServerManagementController {
    private Process rmiServerProcess;
    private final StringBuilder serverLogs = new StringBuilder();
    
    @Value("${server.port:8081}")
    private String serverPort;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getServerStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", rmiServerProcess != null && rmiServerProcess.isAlive() ? "online" : "offline");
        response.put("logs", serverLogs.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startServer() {
        if (rmiServerProcess != null && rmiServerProcess.isAlive()) {
            return createResponse(false, "Server is already running");
        }

        try {
            String projectRoot = System.getProperty("user.dir");
            String classpath = projectRoot + "\\rmi-server\\build\\classes\\java\\main;" +
                             projectRoot + "\\rmi-server\\build\\resources\\main";

            ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-cp", classpath,
                "com.library.rmi.server.LibraryServer"
            );
            processBuilder.redirectErrorStream(true);
            rmiServerProcess = processBuilder.start();

            CompletableFuture.runAsync(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(rmiServerProcess.getInputStream()))) {
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
        if (rmiServerProcess == null || !rmiServerProcess.isAlive()) {
            return createResponse(false, "Server is not running");
        }

        try {
            rmiServerProcess.destroy();
            rmiServerProcess.waitFor();
            serverLogs.append("Server stopped\n");
            return createResponse(true, "Server stopped successfully");
        } catch (Exception e) {
            return createResponse(false, "Failed to stop server: " + e.getMessage());
        }
    }

    @PostMapping("/restart")
    public ResponseEntity<Map<String, Object>> restartServer() {
        ResponseEntity<Map<String, Object>> stopResponse = stopServer();
        if (stopResponse.getBody() != null && Boolean.TRUE.equals(stopResponse.getBody().get("success"))) {
            return startServer();
        }
        return stopResponse;
    }

    private ResponseEntity<Map<String, Object>> createResponse(boolean success, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
}