package com.example.books.api;

import com.example.books.models.response.BooksDetailsResponse;
import com.example.books.models.response.NewBooksResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ITBookstoreAPI {

    @GET("1.0/new")
    Observable<NewBooksResponse> getNewBooks();

    @GET("/1.0/books/{isbn13}")
    Observable<BooksDetailsResponse> getBookDetails(@Path("isbn13") String isbn13);

}
