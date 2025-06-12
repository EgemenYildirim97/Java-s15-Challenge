package com.example.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberRecord {
    private long recordId;
    private Book borrowedBook;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private double fine; // Ceza ücreti 5 birim belirlendi

    public MemberRecord(Book borrowedBook, LocalDate borrowDate) {
        this.borrowedBook = borrowedBook;
        this.borrowDate = borrowDate;
        this.returnDate=null;
        this.fine=5.0;
    }
}
