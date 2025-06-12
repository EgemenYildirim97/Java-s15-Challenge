package com.example.library.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class StudyBook extends Book{
    private String subject;

    public StudyBook(String bookID, String title, String authorName, double price, ItemStatus status, String edition, LocalDate dateOfPurchase, String subject) {
        super(bookID, title, authorName, price, status, edition, dateOfPurchase);
        this.subject = subject;
    }

    @Override
    public String displayDetails() {
        return super.displayDetails() + ", Subject: " + subject;
    }
}
