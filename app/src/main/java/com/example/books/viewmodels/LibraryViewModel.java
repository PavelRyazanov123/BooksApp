package com.example.books.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

public class LibraryViewModel extends ViewModel {

    private static final String TAG = "LibraryViewModel";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ITBookstoreAPI itBookstoreAPI = Network.getInstance().getApi();
    private AppDatabase appDatabase = App.getDatabase();
    private MutableLiveData<List<Book>> bookLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> refreshFlag = new MutableLiveData<>();

    public LiveData<Boolean> getRefreshingFlag() {
        return refreshFlag;
    }

    public LiveData<List<Book>> getBookLiveData() {
        return bookLiveData;
    }

    public LibraryViewModel() {
        compositeDisposable.add(appDatabase
                .bookDao()
                .getAllObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> {
                    if (books.isEmpty())
                        loadBooks();
                    else
                        bookLiveData.setValue(books);
                })
        );
    }


    public void loadBooks() {
        compositeDisposable.add(itBookstoreAPI
                .getNewBooks()
                .doOnSubscribe(d -> refreshFlag.postValue(true))
                .subscribeOn(Schedulers.io())
                .map(NewBooksResponse::getBooks)
                .flatMap(Observable::fromIterable)
                .map(NewBooksResponse.BooksResponse::getIsbn13)
                .switchMap(s -> itBookstoreAPI.getBookDetails(s))
                .toList()
                .observeOn(Schedulers.single())
                .subscribe(list -> {
                            updateDb(list);
                            refreshFlag.postValue(false);
                        },
                        throwable ->
                        {
                            Log.e(TAG, "loadBooks: ", throwable);
                            refreshFlag.postValue(false);
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

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
