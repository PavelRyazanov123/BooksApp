package com.example.books;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.books.models.AppDatabase;
import com.example.books.models.AuthorDao;
import com.example.books.models.BookDao;
import com.example.books.models.entities.Author;
import com.example.books.models.entities.Book;
import com.example.books.models.entities.BookAndAuthor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DaoTest {
    private AppDatabase database;
    private BookDao bookDao;
    private AuthorDao authorDao;
    private List<Book> bookList = new ArrayList<>();
    private List<Author> authorList = new ArrayList<>();

    @Before
    public void createDb() throws Exception {
        database = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().getContext(),
                AppDatabase.class)
                .build();
        bookDao = database.bookDao();
        authorDao = database.authorDao();
        createBooksAndAuthors(10);
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }


    @Test
    public void insertAllAuthorsTest() throws Exception {
        authorDao.insertAll(authorList);
        List<Author> authors = authorDao.getAll();
        assertEquals(10, authors.size());
    }

    @Test
    public void insertAllBooksTest() throws Exception {
        authorDao.insertAll(authorList);
        bookDao.insertAll(bookList);
        List<Book> books = bookDao.getAll();
        assertEquals(10, books.size());
    }

    @Test
    public void updateAuthorsTest() throws Exception {
        authorDao.insertAll(authorList);
        Author author = authorList.get(0);
        author.setName("Author");
        authorDao.update(author);
        List<Author> updatedList = authorDao.getAll();
        assertEquals(author.getName(), updatedList.get(0).getName());
    }

    @Test
    public void updateBooksTest() throws Exception {
        authorDao.insertAll(authorList);
        bookDao.insertAll(bookList);
        Book book = bookDao.getAll().get(0);
        book.setTitle("TITLE");
        bookDao.update(book);
        List<Book> books = bookDao.getAll();
        assertEquals(book.getTitle(), books.get(0).getTitle());
    }

    @Test
    public void getBooksAndAuthorTest() throws Exception {
        authorDao.insertAll(authorList);
        bookDao.insertAll(bookList);
        Book book = bookDao.getAll().get(0);
        int bookID = book.getBookId();
        long authorID = book.getAuthorID();
        String authorName = authorDao.getById(authorID).get(0).getName();
        BookAndAuthor bookAndAuthor = bookDao.getBookAndAuthorTest(bookID);

        assertEquals(book, bookAndAuthor.getBook());
        assertEquals(authorName, bookAndAuthor.getAuthorName());
    }

    @Test
    public void getBooksByAuthorId() throws Exception {
        authorDao.insertAll(authorList);
        bookDao.insertAll(bookList);
        int i = 1;
        bookDao.insert(new Book(
                "title " + i,
                "subtitle " + i,
                i,
                "puplisher" + i,
                "pages " + i,
                "year " + i,
                "rating " + i,
                "desc" + i, "",
                "",
                ""));
        List<Book> books = bookDao.getByAuthorID(i);
        assertEquals(2, books.size());

    }

    private void createBooksAndAuthors(int size) {
        for (int i = 0; i < size; i++) {
            authorList.add(new Author(i, "name " + i));
            bookList.add(new Book(
                    "title " + i,
                    "subtitle " + i,
                    i,
                    "puplisher" + i,
                    "pages " + i,
                    "year " + i,
                    "rating " + i,
                    "desc" + i, "",
                    "",
                    ""));
        }

    }

}
