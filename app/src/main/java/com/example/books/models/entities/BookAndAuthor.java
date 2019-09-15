package com.example.books.models.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class BookAndAuthor {
    @Embedded
    private Book book;

    @ColumnInfo(name = "authorName")
    private String authorName;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
