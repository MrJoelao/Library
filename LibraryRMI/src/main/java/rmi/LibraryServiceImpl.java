package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import model.Book;

public class LibraryServiceImpl extends UnicastRemoteObject implements LibraryService {
    private static final long serialVersionUID = 1L;
    private Map<String, Book> books;

    public LibraryServiceImpl() throws RemoteException {
        super();
        books = new ConcurrentHashMap<>();
        initializeSampleBooks();
    }

    private void initializeSampleBooks() {
        addBook(new Book("1", "Il Nome della Rosa", "Umberto Eco"));
        addBook(new Book("2", "La Divina Commedia", "Dante Alighieri"));
        addBook(new Book("3", "I Promessi Sposi", "Alessandro Manzoni"));
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
                .filter(book -> 
                    book.getTitle().toLowerCase().contains(lowercaseQuery) ||
                    book.getAuthor().toLowerCase().contains(lowercaseQuery))
                .collect(Collectors.toList());
    }
}
