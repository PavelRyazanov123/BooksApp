package com.example.books.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {

    private Retrofit retrofit;
    private String BASE_URL = "https://api.itbook.store/";
    private static Network mInstance;

    private Network() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Network getInstance() {
        if (mInstance == null)
            mInstance = new Network();
        return mInstance;
    }

    public ITBookstoreAPI getApi() {
        return retrofit.create(ITBookstoreAPI.class);
    }
}
