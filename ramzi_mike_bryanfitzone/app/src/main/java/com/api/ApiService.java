package com.api;

import com.models.User;
import com.models.Programme;
import com.models.Seance;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("users")
    Call<List<User>> getUsers();

    @GET("users")
    Call<List<User>> getUserByEmail(
            @Query("email") String email
    );

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") String id);

    @GET("programs")
    Call<List<Programme>> getProgrammes();

    @GET("seances")
    Call<List<Seance>> getSeances();

}