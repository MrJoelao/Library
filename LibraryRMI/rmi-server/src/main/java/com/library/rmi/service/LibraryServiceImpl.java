package com.library.rmi.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.library.rmi.model.Book;

public class LibraryServiceImpl extends UnicastRemoteObject implements LibraryService {
    private final Map<String, Book> books = new HashMap<>();
    private final Map<String, String> users = new HashMap<>(); // username -> password

    public LibraryServiceImpl() throws RemoteException {
        super();
        initializeBooks();
    }

    private void initializeBooks() throws RemoteException {
        // Add test user
        users.put("test", "test");
        addBook(new Book("1", "Il Nome della Rosa", "Umberto Eco", true));
        addBook(new Book("2", "La Divina Commedia", "Dante Alighieri", true));
        addBook(new Book("3", "I Promessi Sposi", "Alessandro Manzoni", true));
    }

    private void addBook(Book book) {
        books.put(book.getId(), book);
    }

    @Override
    public List<Book> getAllBooks() throws RemoteException {
        return new ArrayList<>(books.values());
    }

    @Override
    public Book getBookById(String id) throws RemoteException {
        return books.get(id);
    }

    private boolean isValidPassword(String password) {
        // Almeno 8 caratteri
        if (password.length() < 8) return false;
        
        // Almeno una lettera maiuscola
        if (!password.matches(".*[A-Z].*")) return false;
        
        // Almeno una lettera minuscola
        if (!password.matches(".*[a-z].*")) return false;
        
        // Almeno un numero
        if (!password.matches(".*\\d.*")) return false;
        
        // Almeno un carattere speciale
        if (!password.matches(".*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?].*")) return false;
        
        return true;
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        String storedPassword = users.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        // Verifica se l'username esiste già
        if (users.containsKey(username)) {
            return false;
        }

        // Valida la password
        if (!isValidPassword(password)) {
            return false;
        }

        // Registra il nuovo utente
        users.put(username, password);
        return true;
    }

    @Override
    public boolean reserveBook(String id, String username) throws RemoteException {
        Book book = books.get(id);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            book.setOwner(username);
            return true;
        }
        return false;
    }

    @Override
    public boolean returnBook(String id, String username) throws RemoteException {
        Book book = books.get(id);
        if (book != null && !book.isAvailable() && username.equals(book.getOwner())) {
            book.setAvailable(true);
            book.setOwner(null);
            return true;
        }
        return false;
    }

    @Override
    public List<Book> searchBooks(String query) throws RemoteException {
        String lowercaseQuery = query.toLowerCase();
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowercaseQuery) ||
                        book.getAuthor().toLowerCase().contains(lowercaseQuery))
                .collect(Collectors.toList());
    }
}
