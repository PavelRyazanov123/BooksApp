package com.example.books;

import android.app.Application;

import androidx.room.Room;

import com.example.books.models.AppDatabase;

public class App extends Application {

    private static AppDatabase database;

    public static AppDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(this, AppDatabase.class, "database").build();
    }
}
