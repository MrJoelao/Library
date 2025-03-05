package com.library.web.controller;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.rmi.model.Book;
import com.library.rmi.service.LibraryService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
        try {
            boolean success = libraryService.login(username, password);
            if (success) {
                Map<String, String> response = new HashMap<>();
                response.put("username", username);
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RemoteException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestParam String username, @RequestParam String password) {
        try {
            boolean success = libraryService.register(username, password);
            if (success) {
                // Se la registrazione ha successo, esegui il login automaticamente
                boolean loginSuccess = libraryService.login(username, password);
                if (loginSuccess) {
                    Map<String, String> response = new HashMap<>();
                    response.put("username", username);
                    return ResponseEntity.ok(response);
                }
            }
            return ResponseEntity.badRequest().build();
        } catch (RemoteException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        try {
            List<Book> books = libraryService.getAllBooks();
            return ResponseEntity.ok(books);
        } catch (RemoteException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        try {
            Book book = libraryService.getBookById(id);
            return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
        } catch (RemoteException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/books/{id}/reserve")
    public ResponseEntity<Void> reserveBook(@PathVariable String id, @RequestParam String username) {
        try {
            boolean success = libraryService.reserveBook(id, username);
            return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
        } catch (RemoteException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/books/{id}/return")
    public ResponseEntity<Void> returnBook(@PathVariable String id, @RequestParam String username) {
        try {
            boolean success = libraryService.returnBook(id, username);
            return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
        } catch (RemoteException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/books/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) {
        try {
            List<Book> books = libraryService.searchBooks(query);
            return ResponseEntity.ok(books);
        } catch (RemoteException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
