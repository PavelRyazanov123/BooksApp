package com.example.books.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewBooksResponse {

    @SerializedName("books")
    private List<BooksResponse> books = null;

    public List<BooksResponse> getBooks() {
        return books;
    }

    public void setBooks(List<BooksResponse> books) {
        this.books = books;
    }

    public class BooksResponse {
        @SerializedName("isbn13")
        private String isbn13;

        public String getIsbn13() {
            return isbn13;
        }

        public void setIsbn13(String isbn13) {
            this.isbn13 = isbn13;
        }
    }
}
