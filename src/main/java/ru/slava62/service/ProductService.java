package ru.slava62.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import ru.slava62.dto.Product;

public interface ProductService {
    @POST("products")
    Call<Product> createProduct(@Body Product createProductRequest);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id);

    @PUT("products/{id}")
    Call<Product> updateProduct(@Path("id") int id);
}
