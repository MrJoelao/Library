package com.library.web.controller;

import java.rmi.RemoteException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        try {
            List<Book> books = libraryService.getAllBooks();
            return ResponseEntity.ok(books);
        } catch (RemoteException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        try {
            Book book = libraryService.getBookById(id);
            if (book != null) {
                return ResponseEntity.ok(book);
            }
            return ResponseEntity.notFound().build();
        } catch (RemoteException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<Void> reserveBook(@PathVariable String id) {
        try {
            boolean success = libraryService.reserveBook(id);
            if (success) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (RemoteException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Void> returnBook(@PathVariable String id) {
        try {
            boolean success = libraryService.returnBook(id);
            if (success) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (RemoteException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) {
        try {
            List<Book> books = libraryService.searchBooks(query);
            return ResponseEntity.ok(books);
        } catch (RemoteException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
