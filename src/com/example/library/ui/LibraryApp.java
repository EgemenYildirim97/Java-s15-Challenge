package com.example.library.ui;

import com.example.library.core.LibraryItem;
import com.example.library.data.LibraryDataStore;
import com.example.library.model.Book;
import com.example.library.model.ItemStatus;
import com.example.library.model.StudyBook;
import com.example.library.service.LibraryService;
import com.example.library.service.LibraryServiceImpl;
import com.example.library.util.BillGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class LibraryApp {

    private final Scanner scanner;
    private final LibraryService libraryService;

    public LibraryApp(LibraryService libraryService) {
        this.scanner = new Scanner(System.in);
        this.libraryService = libraryService;
    }

    public void start() {
        System.out.println("Welcome to the Library Management System!");
        int choice;
        do {
            displayMenu();
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            handleUserChoice(choice);

        } while (choice != 0);

        System.out.println("Thank you for using the Library Management System. Goodbye!");
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. List All Items");
        System.out.println("2. Add New Book");
        System.out.println("3. Search Item");
        System.out.println("4. Update Book Info");
        System.out.println("5. Delete Book");
        System.out.println("6. List Books by Category (Subject)");
        System.out.println("7. List Books by Author");
        System.out.println("8. Borrow Book");
        System.out.println("9. Return Book");
        System.out.println("0. Exit");
        System.out.println("-----------------");
    }

    private void handleUserChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    listAllItems();
                    break;
                case 2:
                    addNewBook();
                    break;
                case 3:
                    searchItem();
                    break;
                case 4:
                    updateBookInfo();
                    break;
                case 5:
                    deleteBook();
                    break;
                case 6:
                    listBooksByCategory();
                    break;
                case 7:
                    listBooksByAuthor();
                    break;
                case 8:
                    borrowBook();
                    break;
                case 9:
                    returnBook();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (Exception e) {
            // GlobalExceptionHandler tarafından yakalanacağı için burada tekrar try-catch yapmaya gerek yok
            // Ancak konsol uygulamasında direkt olarak hatayı yakalayıp kullanıcıya gösterebiliriz.
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void listAllItems() {
        List<LibraryItem> items = libraryService.getAllItems();
        if (items.isEmpty()) {
            System.out.println("No items in the library.");
            return;
        }
        System.out.println("\n--- All Library Items ---");
        items.forEach(item -> System.out.println(item.displayDetails()));
    }

    private void addNewBook() {
        System.out.println("\n--- Add New Book ---");
        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author Name: ");
        String author = scanner.nextLine();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Edition: ");
        String edition = scanner.nextLine();
        System.out.print("Is this a Study Book? (yes/no): ");
        String isStudyBook = scanner.nextLine();

        if (isStudyBook.equalsIgnoreCase("yes")) {
            System.out.print("Enter Subject: ");
            String subject = scanner.nextLine();
            libraryService.addItem(new StudyBook(id, title, author, price, ItemStatus.AVAILABLE, edition, LocalDate.now(), subject));
        } else {
            libraryService.addItem(new Book(id, title, author, price, ItemStatus.AVAILABLE, edition, LocalDate.now()));
        }
        System.out.println("Book added successfully.");
    }

    private void searchItem() {
        System.out.println("\n--- Search Item ---");
        System.out.print("Search by (ID/TITLE/AUTHOR): ");
        String searchType = scanner.nextLine();
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();

        List<LibraryItem> results = libraryService.searchItems(query, searchType);
        if (results.isEmpty()) {
            System.out.println("No items found matching your criteria.");
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(item -> System.out.println(item.displayDetails()));
        }
    }

    private void updateBookInfo() {
        System.out.println("\n--- Update Book Info ---");
        System.out.print("Enter Book ID to update: ");
        String id = scanner.nextLine();
        LibraryItem existingItem = libraryService.findItemById(id);

        if (!(existingItem instanceof Book)) {
            System.out.println("Item with ID " + id + " is not a book or not found. Cannot update as a book.");
            return;
        }
        Book existingBook = (Book) existingItem;

        System.out.println("Current Title: " + existingBook.getTitle() + " (Enter new or press Enter to keep)");
        String newTitle = scanner.nextLine();
        if (!newTitle.isEmpty()) existingBook.setTitle(newTitle);

        System.out.println("Current Author: " + existingBook.getAuthorName() + " (Enter new or press Enter to keep)");
        String newAuthor = scanner.nextLine();
        if (!newAuthor.isEmpty()) existingBook.setAuthorName(newAuthor);

        System.out.println("Current Price: " + existingBook.getPrice() + " (Enter new or 0 to keep)");
        double newPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        if (newPrice > 0) existingBook.setPrice(newPrice);

        System.out.println("Current Edition: " + existingBook.getEdition() + " (Enter new or press Enter to keep)");
        String newEdition = scanner.nextLine();
        if (!newEdition.isEmpty()) existingBook.setEdition(newEdition);

        System.out.println("Current Status: " + existingBook.getStatus() + " (AVAILABLE/BORROWED/DAMAGED, or press Enter to keep)");
        String newStatusStr = scanner.nextLine();
        if (!newStatusStr.isEmpty()) {
            try {
                ItemStatus newStatus = ItemStatus.valueOf(newStatusStr.toUpperCase());
                existingBook.setStatus(newStatus);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Keeping current status.");
            }
        }

        libraryService.updateItem(existingBook);
        System.out.println("Book updated successfully: " + existingBook.displayDetails());
    }

    private void deleteBook() {
        System.out.println("\n--- Delete Book ---");
        System.out.print("Enter Book ID to delete: ");
        String id = scanner.nextLine();
        libraryService.deleteItem(id);
        System.out.println("Book deleted successfully.");
    }

    private void listBooksByCategory() {
        System.out.println("\n--- List Books by Category (Subject) ---");
        System.out.print("Enter Subject/Category: ");
        String category = scanner.nextLine();
        List<Book> books = libraryService.getBooksByCategory(category);
        if (books.isEmpty()) {
            System.out.println("No books found for category: " + category);
        } else {
            books.forEach(book -> System.out.println(book.displayDetails()));
        }
    }

    private void listBooksByAuthor() {
        System.out.println("\n--- List Books by Author ---");
        System.out.print("Enter Author Name: ");
        String author = scanner.nextLine();
        List<Book> books = libraryService.getBooksByAuthor(author);
        if (books.isEmpty()) {
            System.out.println("No books found for author: " + author);
        } else {
            books.forEach(book -> System.out.println(book.displayDetails()));
        }
    }

    private void borrowBook() {
        System.out.println("\n--- Borrow Book ---");
        System.out.print("Enter Reader ID: ");
        String readerId = scanner.nextLine();
        System.out.print("Enter Book ID to borrow: ");
        String bookId = scanner.nextLine();
        libraryService.borrowBook(readerId, bookId);
        System.out.println("Book borrowing process completed.");
    }

    private void returnBook() {
        System.out.println("\n--- Return Book ---");
        System.out.print("Enter Reader ID: ");
        String readerId = scanner.nextLine();
        System.out.print("Enter Book ID to return: ");
        String bookId = scanner.nextLine();
        libraryService.returnBook(readerId, bookId);
        System.out.println("Book return process completed.");
    }


    public static void main(String[] args) {
        // Dependencies manual wiring for console app
        LibraryDataStore dataStore = new LibraryDataStore();
        BillGenerator billGenerator = new BillGenerator();
        LibraryService libraryService = new LibraryServiceImpl(dataStore, billGenerator);

        LibraryApp app = new LibraryApp(libraryService);
        app.start();
    }
}