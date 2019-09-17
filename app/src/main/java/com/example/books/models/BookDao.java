package com.example.books.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.books.models.entities.Book;
import com.example.books.models.entities.BookAndAuthor;

import java.util.List;
import io.reactivex.Observable;

@Dao
public interface BookDao {
    @Insert
    void insert(Book book);

    @Insert
    void insertAll(List<Book> bookList);

    @Update
    void update(Book book);

    @Update
    void updateAll(List<Book> bookList);

    @Delete
    void delete(Book book);

    @Delete
    void deleteAll(List<Book> bookList);

    @Query("select * from book ")
    List<Book> getAll();

    @Query("select * from book ")
    Observable<List<Book>> getAllObservable();

    @Query("select * from book where authorID=:id")
    List<Book> getByAuthorID(long id);

    @Query("select *, author.name as authorName" +
            " from book, author where book.authorID == author.id and book.bookId == :id")
    Observable<BookAndAuthor> getBookAndAuthor(long id);

    @Query("select *, author.name as authorName" +
            " from book, author where book.authorID == author.id and book.bookId == :id")
    BookAndAuthor getBookAndAuthorTest(long id);
}
