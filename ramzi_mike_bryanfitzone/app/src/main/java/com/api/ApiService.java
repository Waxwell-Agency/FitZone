package com.api;

import com.models.User;
import com.models.Programme;
import com.models.Seance;
import com.models.Quiz;
import com.models.Aliment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET("quizzes")
    Call<List<Quiz>> getQuizzes();

    @GET("quizzes/{id}")
    Call<Quiz> getQuizById(@Path("id") String id);

    @POST("users")
    Call<User> createUser(@Body User user);

    @GET("aliments")
    Call<List<Aliment>> getAliments();

}