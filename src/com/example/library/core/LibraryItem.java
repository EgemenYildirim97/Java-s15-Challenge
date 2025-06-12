package com.example.library.core;
import com.example.library.model.ItemStatus;

public interface LibraryItem {
    String getItemID();
    String getTitle();
    String getAuthorName();
    double getPrice();
    ItemStatus getStatus();
    void setStatus(ItemStatus status);
    String displayDetails();
    void updateStatus(ItemStatus newStatus);
}
