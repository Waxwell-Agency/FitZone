package com.api;

import com.api.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.2.27:3000/";

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}