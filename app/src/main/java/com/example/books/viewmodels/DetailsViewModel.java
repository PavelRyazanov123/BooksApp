package com.example.books.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.books.models.entities.BookAndAuthor;
import com.example.books.models.repositories.DetailsRepo;


public class DetailsViewModel extends ViewModel {
    private static final String TAG = "DetailsViewModel";
    private DetailsRepo repo = DetailsRepo.getInstance();
    private MutableLiveData<BookAndAuthor> book = new MutableLiveData<>();
    private MutableLiveData<String> event = new MutableLiveData<>();


    public LiveData<BookAndAuthor> getBookLiveData() {
        return book;
    }

    public LiveData<String> getEventLiveData() {
        return event;
    }

    public void getBook(long id) {
        repo.getBookAndAuthor(id, bookAndAuthor -> book.setValue(bookAndAuthor));
    }

    public void updateDescription(String description) {
        if (book.getValue() != null)
            repo.updateDescription(description,book.getValue(),s -> event.postValue(s));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repo.clearDisposable();
        repo = null;
    }
}
