package ru.slava62.util;

import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.slava62.dto.Category;
import ru.slava62.dto.Product;
import ru.slava62.service.CategoryService;
import ru.slava62.service.ProductService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;

@UtilityClass
public class RetrofitUtils {
   HttpLoggingInterceptor logging =  new HttpLoggingInterceptor(new PrettyLogger());

    public Retrofit getRetrofit() throws MalformedURLException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofMinutes(1l))
                .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        return new Retrofit.Builder()
                .baseUrl(ConfigUtils.getBaseUrl())
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

    }

    public CategoryService getCategoryService() throws MalformedURLException{
       return getRetrofit().create(CategoryService.class);
    }
    public ProductService getProductService() throws MalformedURLException{
        return getRetrofit().create(ProductService.class);
    }
    public Response<Product> getProductResponse(long productId, ProductService service) throws IOException {
        return service
                .getProduct((int)productId)
                .execute();
    }

    public Response<Product> createProductResponse(Product product, ProductService service) throws IOException {
        return service
                .createProduct(product)//(int)productId) long productId
                .execute();
    }

    public Response<Product> updateProductResponse(Product product, ProductService service) throws IOException {
        return service
                .updateProduct(product)//(int)productId) long productId
                .execute();
    }

    public Response<Category> getCategoryResponse(Integer categoryId, CategoryService service) throws IOException {
      return service
                .getCategory(categoryId)
                .execute();
    }

    public Response<Category> updateCategoryResponse(Integer categoryId, CategoryService service) throws IOException {
        return service
                .updateCategory(categoryId)
                .execute();
    }

    public Response<Category> deleteCategoryResponse(Integer categoryId, CategoryService service) throws IOException {
        return service
                .deleteCategory(categoryId)
                .execute();
    }

    public Response<Category> createCategoryResponse(Category category, CategoryService service) throws IOException{
        return service
                .createCategory(category)
                .execute();
    }
}
