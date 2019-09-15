package com.example.books.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.books.models.entities.Author;
import com.example.books.models.entities.Book;

@Database(entities = {Book.class, Author.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BookDao bookDao();

    public abstract AuthorDao authorDao();
}
