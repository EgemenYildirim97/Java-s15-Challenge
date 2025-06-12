package com.example.library.util;

import com.example.library.model.Book;
import com.example.library.model.Reader;

public class BillGenerator {
    private static final double BORROW_FEE_RATE = 0.05;
    private static final double RETURN_REFUND_RATE = 0.05;

    public double calculateBorrowFee(Book book){
        return book.getPrice()*BORROW_FEE_RATE;
    }

    public double calculateReturnRefund(Book book){
        return book.getPrice()*RETURN_REFUND_RATE;
    }

    public void generateInvoice(Reader reader, Book book, double amount, String type){
        System.out.println("\n--- Fatura ---");
        System.out.println("Müşteri: " + reader.getName() + " (ID: " + reader.getMemberID() + ")");
        System.out.println("Kitap: " + book.getTitle() + " (ID: " + book.getItemID() + ")");
        System.out.println("İşlem Tipi: " + type); // "Ödünç Alma" veya "İade"
        System.out.println("Tutar: " + String.format("%.2f", amount) + " TL");
        System.out.println("--------------\n");
    }
}
