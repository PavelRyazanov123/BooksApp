package com.example.books.models.repositories;

import android.util.Log;

import com.example.books.App;
import com.example.books.models.AppDatabase;
import com.example.books.models.entities.BookAndAuthor;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsRepo {
    private static final String TAG = "DetailsRepo";
    private static DetailsRepo mInstance;
    private AppDatabase appDatabase = App.getDatabase();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private DetailsRepo() {
    }

    public static DetailsRepo getInstance() {
        if (mInstance == null)
            mInstance = new DetailsRepo();
        return mInstance;
    }

    public void getBookAndAuthor(long id, Callback<BookAndAuthor> bookAndAuthorCallBack) {
        compositeDisposable.add(appDatabase.bookDao()
                .getBookAndAuthor(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bookAndAuthorCallBack::callback,
                        throwable -> Log.e(TAG, "getBook: ",throwable))
        );
    }

    public void updateDescription(String description, BookAndAuthor bookAndAuthor, Callback<String> updatedCallBack) {
        compositeDisposable.add(
                Observable.just(bookAndAuthor)
                        .map(BookAndAuthor::getBook)
                        .filter(book -> !book.getDesc().equals(description))
                        .map(book -> {
                            book.setDesc(description);
                            return book;
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.single())
                        .subscribe(book -> {
                                    appDatabase.bookDao().update(book);
                                    updatedCallBack.callback("Successfully Updated");
                                },
                                throwable -> {
                                    Log.e(TAG, "updateDescription: ", throwable);
                                    updatedCallBack.callback("An error occurred while updating");
                                }));
    }

    public void clearDisposable(){
        compositeDisposable.clear();
    }
}
