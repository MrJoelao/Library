package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import model.Book;
import rmi.LibraryService;

public class LibraryClient {
    public static void main(String[] args) {
        try {
            // Get the RMI registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            
            // Look up the remote object
            LibraryService libraryService = (LibraryService) registry.lookup("LibraryService");
            
            // Test the connection by getting all books
            System.out.println("Libri disponibili:");
            for (Book book : libraryService.getAllBooks()) {
                System.out.println(book);
            }
            
            // Test book reservation
            String bookId = "1";
            if (libraryService.reserveBook(bookId)) {
                System.out.println("Libro con ID " + bookId + " prenotato con successo!");
            } else {
                System.out.println("Impossibile prenotare il libro con ID " + bookId);
            }
            
        } catch (Exception e) {
            System.err.println("Errore del client: " + e.toString());
            e.printStackTrace();
        }
    }
}
