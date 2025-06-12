package com.example.library.service;

import com.example.library.core.LibraryItem;
import com.example.library.model.Book;

import java.util.List;

public interface LibraryService {
    List<LibraryItem> getAllItems();
    LibraryItem findItemById(String itemId);
    List<LibraryItem> searchItems(String query, String searchType);
    void addItem(LibraryItem item);
    void updateItem(LibraryItem item);
    void deleteItem(String itemId);
    List<Book> getBooksByCategory(String category);
    List<Book> getBooksByAuthor(String authorName);

    void borrowBook(String readerId, String bookId);
    void returnBook(String readerId, String bookId);
}
