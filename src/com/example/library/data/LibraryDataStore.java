package com.example.library.data;

import com.example.library.core.LibraryItem;
import com.example.library.model.Book;
import com.example.library.model.ItemStatus;
import com.example.library.model.Reader;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LibraryDataStore {
    private Map<String, LibraryItem> allItems;
    private Map<String, Reader> allReaders;

    public LibraryDataStore(){
        this.allItems=new HashMap<>();
        this.allReaders= new HashMap<>();

        Book book1 = new Book("B001", "The Lord of the Rings", "J.R.R. Tolkien", 25.50, ItemStatus.AVAILABLE, "2nd", LocalDate.of(1995, 1, 1));
        Book book2 = new Book("B002", "Pride and Prejudice", "Jane Austen", 15.00, ItemStatus.AVAILABLE, "1st", LocalDate.of(1813, 1, 28));
        Book book3 = new Book("B003", "1984", "George Orwell", 12.75, ItemStatus.AVAILABLE, "New", LocalDate.of(1949, 6, 8));

        allItems.put(book1.getItemID(), book1);
        allItems.put(book2.getItemID(), book2);
        allItems.put(book3.getItemID(), book3);

        Reader reader1 = new Reader("Ayşe Yılmaz","R001", "İzmir", "555-1111");
        Reader reader2 = new Reader("Mehmet Demir", "R002", "Ankara", "555-2222");

        allReaders.put(reader1.getMemberID(), reader1);
        allReaders.put(reader2.getMemberID(), reader2);
    }
    public void addItem(LibraryItem item){
        allItems.put(item.getItemID(),item);
        System.out.println(item.getTitle() + " added to store.");
    }

    public LibraryItem getItem(String itemId){
        return allItems.get(itemId);
    }

    public void removeItem(String itemId){
        allItems.remove(itemId);
        System.out.println(itemId + " removed from store.");
    }

    public void updatedItem(LibraryItem item){
        if(allItems.containsKey(item.getItemID())){
            allItems.put(item.getItemID(),item);
            System.out.println(item.getTitle() + " updated in store.");
        } else {
            System.out.println("Item with ID " + item.getItemID() + " not found for update.");
        }
    }

    public Reader getReader(String readerId){
        return allReaders.get(readerId);
    }

    public Collection<LibraryItem> getAllItems(){
        return allItems.values();
    }

    public Collection<Reader> getAllReaders(){
        return allReaders.values();
    }
}
