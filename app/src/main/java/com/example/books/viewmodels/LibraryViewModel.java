package com.example.books.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.books.models.repositories.LibraryRepo;
import com.example.books.models.entities.Book;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class LibraryViewModel extends ViewModel {

    private static final String TAG = "LibraryViewModel";
    private MutableLiveData<List<Book>> bookLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> refreshFlag = new MutableLiveData<>();
    private LibraryRepo repo = LibraryRepo.getInstance();

    public LiveData<Boolean> getRefreshingFlag() {
        return refreshFlag;
    }

    public LiveData<List<Book>> getBookLiveData() {
        return bookLiveData;
    }

    public LibraryViewModel() {
        repo.getBooks(bookList -> bookLiveData.setValue(bookList)
        , isRefreshing -> refreshFlag.postValue(isRefreshing));
    }


    public void loadBooks() {
        repo.loadBooks(isRefreshing -> refreshFlag.postValue(isRefreshing));
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        repo.clearDisposable();
        repo = null;
    }
}
