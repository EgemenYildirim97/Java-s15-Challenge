package com.example.library.model;

import com.example.library.core.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true,of = "memberID")
public class Reader extends Person {
    private String memberID;
    private String address;
    private String phoneNumber;
    private LocalDate dateOfMembership;
    private final int MAX_BOOK_LIMIT = 5;
    private Set<Book> borrowedBooks;
    private List<MemberRecord> records;

    public Reader(String name, String memberID, String address, String phoneNumber) {
        super(name);
        this.memberID = memberID;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.dateOfMembership = LocalDate.now();
        this.borrowedBooks = new HashSet<>();
        this.records = new LinkedList<>();
    }

    @Override
    public String getRole() {
        return "Reader";
    }

    public boolean canBorrowMoreBooks(){
        return borrowedBooks.size() < MAX_BOOK_LIMIT;
    }

    public void borrowBook(Book book){
        borrowedBooks.add(book);
        MemberRecord record = new MemberRecord(book, LocalDate.now());
        records.add(record);
        System.out.println(this.getName() + " borrowed: " + book.getTitle());
    }

    public void returnBook(Book book){
        borrowedBooks.remove(book);
        records.stream()
                .filter(rec -> rec.getBorrowedBook().equals(book)&& rec.getReturnDate()==null)
                .findFirst()
                .ifPresent(rec -> rec.setReturnDate(LocalDate.now()));
        System.out.println(this.getName() + " returned: " + book.getTitle());
    }
}
