package ru.slava62.service;


import retrofit2.Call;
import retrofit2.http.*;
import ru.slava62.dto.Category;


public interface CategoryService {
    @GET("categories/{id}")
    Call<Category> getCategory(@Path("id") Integer id);
    @DELETE("categories/{id}")
    Call<Category> deleteCategory(@Path("id") Integer id);
    @PUT("categories/{id}")
    Call<Category> updateCategory(@Path("id") Integer id);
    @POST("categories")
    Call<Category> createCategory(@Body Category category);
}
