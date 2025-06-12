package com.example.library.service;


import com.example.library.core.LibraryItem;
import com.example.library.data.LibraryDataStore;
import com.example.library.exception.HttpStatusCode;
import com.example.library.exception.StudentException;
import com.example.library.model.Book;
import com.example.library.model.ItemStatus;
import com.example.library.model.Reader;
import com.example.library.model.StudyBook;
import com.example.library.util.BillGenerator;

import java.util.List;
import java.util.stream.Collectors;

public class LibraryServiceImpl implements LibraryService{
    private final LibraryDataStore dataStore;
    private final BillGenerator billGenerator;

    public LibraryServiceImpl(LibraryDataStore dataStore, BillGenerator billGenerator) {
        this.dataStore = dataStore;
        this.billGenerator = billGenerator;
    }

    @Override
    public List<LibraryItem> getAllItems() {
        return dataStore.getAllItems().stream().collect(Collectors.toList());
    }

    @Override
    public LibraryItem findItemById(String itemId) {
        LibraryItem item = dataStore.getItem(itemId);
        if (item == null) {
            throw new StudentException("Item with ID " + itemId + " not found!", HttpStatusCode.BAD_REQUEST);
        }
        return item;
    }

    @Override
    public List<LibraryItem> searchItems(String query, String searchType) {
        List<LibraryItem> results = dataStore.getAllItems().stream().filter(item -> {
            switch (searchType.toUpperCase()) {
                case "ID": return item.getItemID().equalsIgnoreCase(query);
                case "TITLE": return item.getTitle().toLowerCase().contains(query.toLowerCase());
                case "AUTHOR": return item.getAuthorName().toLowerCase().contains(query.toLowerCase());
                default: return false;
            }
        }).collect(Collectors.toList());

        if (results.isEmpty()) {
            throw new StudentException("No items found for '" + query + "' with search type '" + searchType + "'", HttpStatusCode.NOT_FOUND);
        }
        return results;
    }

    @Override
    public void addItem(LibraryItem item) {
        if (dataStore.getItem(item.getItemID()) != null) {
            throw new StudentException("Item with ID " + item.getItemID() + " already exists!", HttpStatusCode.CONFLICT);
        }
        dataStore.addItem(item);
    }

    @Override
    public void updateItem(LibraryItem item) {
        if (dataStore.getItem(item.getItemID()) == null) {
            throw new StudentException("Item with ID " + item.getItemID() + " not found for update!", HttpStatusCode.NOT_FOUND);
        }
        dataStore.updatedItem(item);
    }

    @Override
    public void deleteItem(String itemId) {

        LibraryItem item = dataStore.getItem(itemId);
        if (item == null) {
            throw new StudentException("Item with ID " + itemId + " not found for deletion!", HttpStatusCode.NOT_FOUND);
        }
        // Eğer kitap ödünç alınmışsa silinmesine izin verme
        if (item.getStatus() == ItemStatus.BORROWED) {
            throw new StudentException("Cannot delete item with ID " + itemId + " as it is currently borrowed!", HttpStatusCode.BAD_REQUEST);
        }
        dataStore.removeItem(itemId);
    }

    @Override
    public List<Book> getBooksByCategory(String category) {
        return dataStore.getAllItems().stream()
                .filter(item -> item instanceof StudyBook) // Sadece StudyBook'ları filtrele
                .map(item -> (StudyBook) item)             // StudyBook'a dönüştür
                .filter(studyBook -> studyBook.getSubject().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getBooksByAuthor(String authorName) {
        return dataStore.getAllItems().stream()
                .filter(item -> item instanceof Book) // Sadece Book'ları filtrele
                .map(item -> (Book) item)             // Book'a dönüştür
                .filter(book -> book.getAuthorName().equalsIgnoreCase(authorName))
                .collect(Collectors.toList());
    }

    @Override
    public void borrowBook(String readerId, String bookId) {

        Reader reader = dataStore.getReader(readerId);
        if (reader == null) {
            throw new StudentException("Reader with ID " + readerId + " not found!", HttpStatusCode.NOT_FOUND);
        }

        LibraryItem item = dataStore.getItem(bookId);
        if (!(item instanceof Book)) {
            throw new StudentException("Item with ID " + bookId + " is not a borrowable Book!", HttpStatusCode.BAD_REQUEST);
        }
        Book book = (Book) item;

        if (book.getStatus() == ItemStatus.BORROWED) {
            throw new StudentException("Book '" + book.getTitle() + "' is already borrowed!", HttpStatusCode.CONFLICT);
        }

        if (!reader.canBorrowMoreBooks()) {
            throw new StudentException("Reader " + reader.getName() + " has reached the maximum book limit (" + reader.getBorrowedBooks().size() + "/" + reader.getMAX_BOOK_LIMIT() + ")", HttpStatusCode.BAD_REQUEST);
        }

        book.updateStatus(ItemStatus.BORROWED);
        reader.borrowBook(book); // Reader'ın kendi metodunu kullan

        double fee = billGenerator.calculateBorrowFee(book);
        billGenerator.generateInvoice(reader, book, fee, "Ödünç Alma");
        System.out.println("Book '" + book.getTitle() + "' borrowed by " + reader.getName());
    }

    @Override
    public void returnBook(String readerId, String bookId) {

        Reader reader = dataStore.getReader(readerId);
        if (reader == null) {
            throw new StudentException("Reader with ID " + readerId + " not found!", HttpStatusCode.NOT_FOUND);
        }

        LibraryItem item = dataStore.getItem(bookId);
        if (!(item instanceof Book)) {
            throw new StudentException("Item with ID " + bookId + " is not a Book!", HttpStatusCode.BAD_REQUEST);
        }
        Book book = (Book) item;

        if (book.getStatus() == ItemStatus.AVAILABLE) {
            throw new StudentException("Book '" + book.getTitle() + "' is already available!", HttpStatusCode.BAD_REQUEST);
        }
        if (!reader.getBorrowedBooks().contains(book)) {
            throw new StudentException("Reader '" + reader.getName() + "' did not borrow this book!", HttpStatusCode.BAD_REQUEST);
        }

        book.updateStatus(ItemStatus.AVAILABLE);
        reader.returnBook(book); // Reader'ın kendi metodunu kullan

        double refund = billGenerator.calculateReturnRefund(book);
        billGenerator.generateInvoice(reader, book, refund, "İade");
        System.out.println("Book '" + book.getTitle() + "' returned by " + reader.getName());

    }
}
