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
        
        // Classici Italiani
        addBook(new Book("1", "Il Nome della Rosa", "Umberto Eco", true));
        addBook(new Book("2", "La Divina Commedia", "Dante Alighieri", true));
        addBook(new Book("3", "I Promessi Sposi", "Alessandro Manzoni", true));
        addBook(new Book("4", "Decameron", "Giovanni Boccaccio", true));
        addBook(new Book("5", "Il Gattopardo", "Giuseppe Tomasi di Lampedusa", true));
        addBook(new Book("41", "Se questo è un uomo", "Primo Levi", true));
        addBook(new Book("42", "Il fu Mattia Pascal", "Luigi Pirandello", true));
        addBook(new Book("43", "Zeno Cosini", "Italo Svevo", true));
        addBook(new Book("44", "Le città invisibili", "Italo Calvino", true));
        addBook(new Book("45", "Il Barone Rampante", "Italo Calvino", true));
        
        // Letteratura Italiana Contemporanea
        addBook(new Book("46", "La solitudine dei numeri primi", "Paolo Giordano", true));
        addBook(new Book("47", "Acciaio", "Silvia Avallone", true));
        addBook(new Book("48", "Non ti muovere", "Margaret Mazzantini", true));
        addBook(new Book("49", "Caos Calmo", "Sandro Veronesi", true));
        addBook(new Book("50", "Le otto montagne", "Paolo Cognetti", true));
        
        // Letteratura Internazionale
        addBook(new Book("6", "1984", "George Orwell", true));
        addBook(new Book("7", "Don Chisciotte", "Miguel de Cervantes", true));
        addBook(new Book("8", "Guerra e Pace", "Leo Tolstoy", true));
        addBook(new Book("9", "L'Odissea", "Homer", true));
        addBook(new Book("10", "Cent'anni di Solitudine", "Gabriel García Márquez", true));
        
        // Letteratura Moderna
        addBook(new Book("11", "L'Alchimista", "Paulo Coelho", true));
        addBook(new Book("12", "Norwegian Wood - Tokyo Blues", "Haruki Murakami", true));
        addBook(new Book("13", "L'Ombra del Vento", "Carlos Ruiz Zafón", true));
        addBook(new Book("14", "Il Cacciatore di Aquiloni", "Khaled Hosseini", true));
        addBook(new Book("15", "Vita di Pi", "Yann Martel", true));

        // Narrativa Contemporanea
        addBook(new Book("16", "La Biblioteca di Mezzanotte", "Matt Haig", true));
        addBook(new Book("17", "La Ragazza dei Gigli", "Delia Owens", true));
        addBook(new Book("18", "I Sette Mariti di Evelyn Hugo", "Taylor Jenkins Reid", true));
        addBook(new Book("19", "Circe", "Madeline Miller", true));
        addBook(new Book("20", "Il Paziente Silenzioso", "Alex Michaelides", true));

        // Fantascienza
        addBook(new Book("21", "Progetto Hail Mary", "Andy Weir", true));
        addBook(new Book("22", "Dune", "Frank Herbert", true));
        addBook(new Book("23", "Il Problema dei Tre Corpi", "Liu Cixin", true));
        addBook(new Book("24", "Neuromante", "William Gibson", true));
        addBook(new Book("25", "Fondazione", "Isaac Asimov", true));

        // Gialli e Thriller
        addBook(new Book("26", "Il Club del Delitto del Giovedì", "Richard Osman", true));
        addBook(new Book("27", "L'Amore Bugiardo", "Gillian Flynn", true));
        addBook(new Book("28", "Uomini che Odiano le Donne", "Stieg Larsson", true));
        addBook(new Book("29", "Dieci Piccoli Indiani", "Agatha Christie", true));
        addBook(new Book("30", "Il Codice Da Vinci", "Dan Brown", true));

        // Saggistica
        addBook(new Book("31", "Sapiens: Da Animali a Dèi", "Yuval Noah Harari", true));
        addBook(new Book("32", "Abitudini Atomiche", "James Clear", true));
        addBook(new Book("33", "Dal Big Bang ai Buchi Neri", "Stephen Hawking", true));
        addBook(new Book("34", "L'Educazione", "Tara Westover", true));
        addBook(new Book("35", "Il Diario di Anna Frank", "Anne Frank", true));

        // Poesia
        addBook(new Book("36", "Latte e Miele", "Rupi Kaur", true));
        addBook(new Book("37", "Il Sole e i Suoi Fiori", "Rupi Kaur", true));
        addBook(new Book("38", "La Collina che Scaliamo", "Amanda Gorman", true));
        addBook(new Book("39", "La Terra Desolata", "T.S. Eliot", true));
        addBook(new Book("40", "Foglie d'Erba", "Walt Whitman", true));
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
