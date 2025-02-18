package com.library.rmi.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.library.rmi.service.LibraryService;
import com.library.rmi.service.LibraryServiceImpl;

public class LibraryServer {
    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", "localhost");
            
            // Create and export the registry at the default port 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            
            // Create the remote service
            LibraryService libraryService = new LibraryServiceImpl();
            
            // Bind the remote service to the registry
            registry.rebind("LibraryService", libraryService);
            
            System.out.println("Server RMI della biblioteca avviato sulla porta 1099");
            System.out.println("Premi Ctrl+C per terminare");
        } catch (Exception e) {
            System.err.println("Errore del server: " + e.toString());
            e.printStackTrace();
        }
    }
}
