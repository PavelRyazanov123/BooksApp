package com.example.books.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.books.App;
import com.example.books.models.AppDatabase;
import com.example.books.models.entities.BookAndAuthor;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsViewModel extends ViewModel {
    private static final String TAG = "DetailsViewModel";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<BookAndAuthor> book = new MutableLiveData<>();
    private AppDatabase appDatabase = App.getDatabase();

    public LiveData<BookAndAuthor> getBookLiveData() {
        return book;
    }

    public void getBook(long id) {
        compositeDisposable.add(appDatabase.bookDao()
                .getBookAndAuthor(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bookAndAuthor -> book.setValue(bookAndAuthor),
                        throwable -> Log.e(TAG, "getBook: "))
        );
    }

    public void updateDescription(String description) {
        compositeDisposable.add(
                Observable.just(book.getValue())
                        .filter(bookAndAuthor -> bookAndAuthor != null)
                        .map(BookAndAuthor::getBook)
                        .filter(book -> !book.getDesc().equals(description))
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .map(book -> {
                            book.setDesc(description);
                            return book;
                        })
                        .observeOn(Schedulers.single())
                        .subscribe(book -> appDatabase.bookDao().update(book),
                                throwable -> Log.e(TAG, "updateDescription: ", throwable))
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
