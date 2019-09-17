package com.example.books.models.repositories;

import android.util.Log;

import com.example.books.App;
import com.example.books.api.ITBookstoreAPI;
import com.example.books.api.Network;
import com.example.books.models.AppDatabase;
import com.example.books.models.entities.Author;
import com.example.books.models.entities.Book;
import com.example.books.models.response.BooksDetailsResponse;
import com.example.books.models.response.NewBooksResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LibraryRepo {
    private static final String TAG = "LibraryRepo";
    private static LibraryRepo mInstance;
    private ITBookstoreAPI itBookstoreAPI = Network.getInstance().getApi();
    private AppDatabase appDatabase = App.getDatabase();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static LibraryRepo getInstance() {
        if (mInstance == null)
            mInstance = new LibraryRepo();
        return mInstance;
    }

    private LibraryRepo() { }

    public void getBooks(Callback<List<Book>> bookCallback, Callback<Boolean> refreshingCallback) {
        compositeDisposable.add(appDatabase
                .bookDao()
                .getAllObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> {
                    if (books.isEmpty())
                        loadBooks(refreshingCallback);
                    else
                        bookCallback.callback(books);
                })
        );
    }

    public void loadBooks(Callback<Boolean> callback) {
        compositeDisposable.add(itBookstoreAPI
                .getNewBooks()
                .doOnSubscribe(d -> callback.callback(true))
                .subscribeOn(Schedulers.io())
                .map(NewBooksResponse::getBooks)
                .flatMap(Observable::fromIterable)
                .map(NewBooksResponse.BooksResponse::getIsbn13)
                .switchMap(s -> itBookstoreAPI.getBookDetails(s))
                .toList()
                .observeOn(Schedulers.single())
                .subscribe(list -> {
                            updateDb(list);
                            callback.callback(false);
                        },
                        throwable ->
                        {
                            Log.e(TAG, "loadBooks: ", throwable);
                            callback.callback(false);
                        })
        );
    }

    private List<Book> insertedBooksList = new ArrayList<>();
    private List<Author> insertedAuthorsList = new ArrayList<>();

    private void updateDb(List<BooksDetailsResponse> list) {
        insertedAuthorsList.clear();
        insertedBooksList.clear();
        for (BooksDetailsResponse response : list) {
            List<Author> authors = appDatabase.authorDao().getById(response.getIsbn13());
            List<Book> books = appDatabase.bookDao().getByAuthorID(response.getIsbn13());
            if (authors.isEmpty())
                insertedAuthorsList.add(new Author(response.getIsbn13(), response.getAuthors()));
            if (books.isEmpty())
                insertedBooksList.add(new Book(
                        response.getTitle(),
                        response.getSubtitle(),
                        response.getIsbn13(),
                        response.getPublisher(),
                        response.getPages(),
                        response.getYear(),
                        response.getRating(),
                        response.getDesc(),
                        response.getPrice(),
                        response.getImage(),
                        response.getUrl()));
        }
        if (!insertedAuthorsList.isEmpty()) {
            appDatabase.authorDao().insertAll(insertedAuthorsList);
        }
        if (!insertedBooksList.isEmpty())
            appDatabase.bookDao().insertAll(insertedBooksList);
    }

    public void clearDisposable(){
        compositeDisposable.clear();
    }
}
