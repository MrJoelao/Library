package com.library.rmi.server;

import com.library.rmi.service.LibraryService;
import com.library.rmi.service.LibraryServiceImpl;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class LibraryServer {
    private static final Properties properties = new Properties();
    private static Registry registry;
    private static LibraryService libraryService;
    private static final int DEFAULT_PORT = 1099;

    public static void main(String[] args) {
        try {
            // Load properties
            loadProperties();
            
            // Get hostname configuration
            String configuredHost = System.getProperty("RMI_SERVER_HOST", properties.getProperty("rmi.server.hostname", "auto"));
            String hostname = determineHostname(configuredHost);
            
            System.out.println("Server RMI della biblioteca in avvio su " + hostname + ":" + DEFAULT_PORT);
            System.setProperty("java.rmi.server.hostname", hostname);
            
            // Try to create registry with default port
            try {
                registry = LocateRegistry.createRegistry(DEFAULT_PORT);
            } catch (java.rmi.server.ExportException e) {
                // If port is already in use, try to get the existing registry
                System.out.println("Registry già esistente sulla porta " + DEFAULT_PORT + ", tentativo di connessione...");
                registry = LocateRegistry.getRegistry(DEFAULT_PORT);
            }
            
            // Create and export the remote service
            libraryService = new LibraryServiceImpl();
            
            // Bind the remote service to the registry
            registry.rebind("LibraryService", libraryService);
            
            System.out.println("Server RMI disponibile su " + hostname + ":" + DEFAULT_PORT);

            // Add shutdown hook for cleanup
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (libraryService != null) {
                        UnicastRemoteObject.unexportObject(libraryService, true);
                    }
                    if (registry != null) {
                        UnicastRemoteObject.unexportObject(registry, true);
                    }
                    System.out.println("\nServer RMI terminato");
                } catch (Exception e) {
                    System.err.println("Errore durante la chiusura del server: " + e.toString());
                }
            }));
        } catch (Exception e) {
            System.err.println("Errore del server: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void loadProperties() {
        try (InputStream input = LibraryServer.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("application.properties non trovato, uso valori di default");
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            System.out.println("Errore nel caricamento delle properties: " + ex.getMessage());
        }
    }

    private static String determineHostname(String configuredHost) {
        // Check if property wasn't resolved
        if (configuredHost == null || configuredHost.contains("${")) {
            return "auto";
        }
        
        // Se è specificato un hostname specifico (non "auto"), usalo
        if (!"auto".equals(configuredHost)) {
            return configuredHost;
        }

        try {
            // Prova a ottenere l'hostname del sistema
            String hostname = InetAddress.getLocalHost().getHostAddress();
            // Se siamo su localhost, usa esplicitamente "localhost"
            if (hostname.equals("127.0.0.1") || hostname.equals("0:0:0:0:0:0:0:1")) {
                return "localhost";
            }
            return hostname;
        } catch (UnknownHostException e) {
            System.out.println("Impossibile determinare l'hostname, uso localhost: " + e.getMessage());
            return "localhost";
        }
    }
}
