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
    private static final long serialVersionUID = 1L;
    private final Map<String, Book> books;

    public LibraryServiceImpl() throws RemoteException {
        books = new HashMap<>();
        initializeBooks();
    }

    private void initializeBooks() {
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

    @Override
    public boolean reserveBook(String id) throws RemoteException {
        Book book = books.get(id);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean returnBook(String id) throws RemoteException {
        Book book = books.get(id);
        if (book != null && !book.isAvailable()) {
            book.setAvailable(true);
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
