package com.example.books.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.books.models.entities.Author;

import java.util.List;

import io.reactivex.Observable;


@Dao
public interface AuthorDao {
    @Insert
    void insert(Author author);

    @Insert
    void insertAll(List<Author> authorList);

    @Update
    void update(Author author);

    @Update
    void updateAll(List<Author> authorList);

    @Delete
    void delete(Author author);

    @Delete
    void deleteAll(List<Author> authorList);

    @Query("select * from author ")
    List<Author> getAll();

    @Query("select * from author ")
    Observable<List<Author>> getAllObservable();

    @Query("select * from author where id=:id")
    List<Author> getById(long id);
}
