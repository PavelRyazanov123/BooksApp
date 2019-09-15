package com.example.books.models.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Author.class, parentColumns = "id", childColumns = "authorID"))
public class Book {
    @PrimaryKey(autoGenerate = true)
    private int bookId;
    private String title;
    private String subtitle;
    private long authorID;
    private String publisher;
    private String pages;
    private String year;
    private String rating;
    private String desc;
    private String price;
    private String image;
    private String url;

    public Book(String title, String subtitle, long authorID, String publisher, String pages, String year, String rating, String desc, String price, String image, String url) {
        this.title = title;
        this.subtitle = subtitle;
        this.authorID = authorID;
        this.publisher = publisher;
        this.pages = pages;
        this.year = year;
        this.rating = rating;
        this.desc = desc;
        this.price = price;
        this.image = image;
        this.url = url;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public long getAuthorID() {
        return authorID;
    }

    public void setAuthorID(long authorID) {
        this.authorID = authorID;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
