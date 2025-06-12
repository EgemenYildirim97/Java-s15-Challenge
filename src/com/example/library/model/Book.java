package com.example.library.model;

import com.example.library.core.LibraryItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "bookID")
public class Book implements LibraryItem {
    private String bookID;
    private String title;
    private String authorName;
    private double price;
    private ItemStatus status;
    private String edition;
    private LocalDate dateOfPurchase;

    @Override
    public String getItemID(){
        return this.bookID;
    }

    @Override
    public String displayDetails() {
        return "Book ID: " + bookID + ", Title: " + title + ", Author: " + authorName +
                ", Price: " + price + ", Status: " + status + ", Edition: " + edition;
    }

    @Override
    public void updateStatus(ItemStatus newStatus) {
        this.status = newStatus;
        System.out.println("Book '" + title + "' status updated to: " + newStatus);
    }
}
