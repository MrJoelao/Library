package com.library.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.library.rmi.model.Book;

public interface LibraryService extends Remote {
    List<Book> getAllBooks() throws RemoteException;
    Book getBookById(String id) throws RemoteException;
    boolean login(String username, String password) throws RemoteException;
    boolean register(String username, String password) throws RemoteException;
    boolean reserveBook(String id, String username) throws RemoteException;
    boolean returnBook(String id, String username) throws RemoteException;
    List<Book> searchBooks(String query) throws RemoteException;
}
