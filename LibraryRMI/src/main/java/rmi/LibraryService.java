package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.Book;

public interface LibraryService extends Remote {
    List<Book> getAllBooks() throws RemoteException;
    Book getBookById(String id) throws RemoteException;
    boolean reserveBook(String id) throws RemoteException;
    boolean returnBook(String id) throws RemoteException;
    List<Book> searchBooks(String query) throws RemoteException;
}
