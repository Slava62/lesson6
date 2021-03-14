package ru.slava62;

import com.github.javafaker.Faker;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Converter;
import ru.slava62.db.model.Products;
import ru.slava62.enums.CategoryType;
import ru.slava62.dto.ErrorBody;
import ru.slava62.dto.Product;
import ru.slava62.db.dao.ProductsMapper;
import ru.slava62.service.ProductService;
import ru.slava62.util.DbUtils;
import ru.slava62.util.RetrofitUtils;

import java.lang.annotation.Annotation;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


@Epic("Market tests")
@Feature("Product test-suite")
public class ProductTests {
    Integer productId;
    Faker faker = new Faker();
    static ProductService productService;
    static ProductsMapper productsMapper;
    Product product;

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        productsMapper = DbUtils.getProductsMapper();
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withCategoryTitle(CategoryType.FOOD.getTitle())
                .withPrice((int) (Math.random() * 1000 + 1))
                .withTitle(faker.food().ingredient());
    }

    @SneakyThrows
    @Test
    void createNewProductTest() {
        /*Products record=new Products();
        record.setTitle("test_product");
        record.setCategory_id(3L);
        record.setPrice(300);
        productsMapper.insert(record);*/
        retrofit2.Response<Product> response =
                productService.createProduct(product)
                        .execute();
        productId = response.body().getId();
        boolean b= response.isSuccessful();
        assertThat(b, is(true));
        // assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getTitle()).isEqualTo(product.getTitle());
    }

    @SneakyThrows
    @Test
    void getProductTest() {
        retrofit2.Response<Product> response =
                productService.getProduct(3474)//product.getId())
                        .execute();
        productId = response.body().getId();
        boolean b= response.isSuccessful();
        assertThat(b, is(true));
        // assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getTitle()).isEqualTo(product.getTitle());
    }
    @SneakyThrows
    @Test
    void createNewProductNegativeTest() {
        retrofit2.Response<Product> response =
                productService.createProduct(product.withId(555))
                        .execute();
//        productId = Objects.requireNonNull(response.body()).getId();
        assertThat(response.code(), is(equalTo(400)));
        if (response != null && !response.isSuccessful() && response.errorBody() != null) {
            ResponseBody body = response.errorBody();
            Converter<ResponseBody, ErrorBody> converter = RetrofitUtils.getRetrofit().responseBodyConverter(ErrorBody.class, new Annotation[0]);
            ErrorBody errorBody = converter.convert(body);
            assertThat(errorBody.getMessage(),is(equalTo("Id must be null for new entity")));
        }
    }

    @AfterEach
    void tearDown() {
        if (productId!=null)
            DbUtils.getCategoriesMapper().deleteByPrimaryKey(productId);
    }
}
