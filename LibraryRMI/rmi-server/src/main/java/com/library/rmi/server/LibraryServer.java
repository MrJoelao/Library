package com.library.rmi.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.library.rmi.service.LibraryService;
import com.library.rmi.service.LibraryServiceImpl;

public class LibraryServer {
    private static Registry registry;
    private static LibraryService libraryService;
    private static final int DEFAULT_PORT = 1099;

    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", "localhost");
            
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
            
            System.out.println("Server RMI della biblioteca avviato sulla porta " + DEFAULT_PORT);
            System.out.println("Premi Ctrl+C per terminare");

            // Add shutdown hook for cleanup
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (libraryService != null) {
                        UnicastRemoteObject.unexportObject(libraryService, true);
                    }
                    if (registry != null) {
                        UnicastRemoteObject.unexportObject(registry, true);
                    }
                    System.out.println("\nServer RMI terminato con successo");
                } catch (Exception e) {
                    System.err.println("Errore durante la chiusura del server: " + e.toString());
                }
            }));
        } catch (Exception e) {
            System.err.println("Errore del server: " + e.toString());
            e.printStackTrace();
        }
    }
}
