package ru.slava62.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import ru.slava62.dto.Product;

public interface ProductService {
    @POST("products")
    Call<Product> createProduct(@Body Product createProductRequest);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") long id);

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") long id);

    @PUT("products")
    Call<Product> updateProduct(@Body Product updateProductRequest);

    @GET("products")
    Call<ResponseBody> getAllProducts();
}
