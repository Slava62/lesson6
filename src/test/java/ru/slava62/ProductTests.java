package ru.slava62;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.*;
import retrofit2.Converter;
import retrofit2.Response;
import ru.slava62.db.dao.CategoriesMapper;
import ru.slava62.db.model.Products;
import ru.slava62.db.model.ProductsExample;
import ru.slava62.enums.CategoryType;
import ru.slava62.dto.ErrorBody;
import ru.slava62.dto.Product;
import ru.slava62.db.dao.ProductsMapper;
import ru.slava62.service.ProductService;
import ru.slava62.util.DbUtils;
import ru.slava62.util.RetrofitUtils;

import java.lang.annotation.Annotation;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


@Epic("Market tests")
@Feature("Product test-suite")
public class ProductTests {
  //  Integer productId;
    static ProductService productService;
    static ProductsMapper productsMapper;
    static CategoriesMapper categoriesMapper;
    static Products productForTest;
    Faker faker = new Faker();

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        productsMapper = DbUtils.getProductsMapper();
        productService = RetrofitUtils.getProductService();
        categoriesMapper = DbUtils.getCategoriesMapper();
        productForTest=null;
    }

    @BeforeEach
    void setUp() {
        productForTest=new Products();
        productForTest.setPrice((int) (Math.random() * 1000 + 1));
        productForTest.setTitle("test_product");//faker.food().spice());
        productForTest.setCategory_id((long) categoriesMapper.selectByPrimaryKey((int)(Math.random() * 3 + 1)).getId());//(long) (Math.random() * 10 + 1));
        productsMapper.insert(productForTest);
       /* product = new Product()
                .withCategoryTitle(CategoryType.FOOD.getTitle())
                .withPrice((int) (Math.random() * 1000 + 1))
                .withTitle(faker.food().ingredient());*/
    }


   /* void createNewProductTest() {
        *//*Products record=new Products();
        record.setTitle("test_product");
        record.setCategory_id(3L);
        record.setPrice(300);
        productsMapper.insert(record);*//*
        Response<Product> response =
                productService.createProduct(product)
                        .execute();
        productId = response.body().getId();
        boolean b= response.isSuccessful();
        assertThat(b, is(true));
        // assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getTitle()).isEqualTo(product.getTitle());
    }*/

    @SneakyThrows
    @Test
    @DisplayName("New product creation test")
    @Description("Check that the product added to the database is available")
    void getNewProductTest() {
        Response<Product> response=RetrofitUtils.getProductResponse(productForTest.getId(),productService);
        step("Check successful response");
        assertThat(response.isSuccessful(), is(true));
        step("Check product was added in DB");
        assertThat(productForTest.getId(),equalTo(response.body().getId()));
        step("Check title of product");
        assertThat(productForTest.getTitle(),equalTo(response.body().getTitle()));
        step("Check price of product");
        assertThat(productForTest.getPrice(),equalTo(response.body().getPrice()));
        // assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getTitle()).isEqualTo(product.getTitle());
    }
    @SneakyThrows
    @Test
    @DisplayName("Update product negative test")
    @Description("Check that the product added to the database can't be updated")
    void updateNewProductTest() {
        Product product=new Product(productForTest.getId(),"test_product_updated",100,1L);
        Response<Product> response=RetrofitUtils.updateProductResponse(product,productService);
        step("Check response code 500");
        assertThat(response.code(), is(500));
        step("Check response error is \"Internal Server Error\"");
        assertThat(ErrorBody.getErorrMessage(response),is(equalTo("Internal Server Error")));
    }
 @SneakyThrows
    @Test
 @DisplayName("Create new product negative test")
 @Description("Check that the product  can't be  added")
    void createNewProductNegativeTest() {
     Product product=new Product(null,
             productForTest.getTitle(),productForTest.getPrice(),
             productForTest.getCategory_id());
     Response<Product> response=RetrofitUtils.createProductResponse(product,productService);
     step("Check response code 500");
     assertThat(response.code(), is(equalTo(500)));
    // productsMapper.deleteByPrimaryKey(response.body().getId());
    }

    @AfterEach
    void tearDown() {
      //  if (productId!=null)
      //      DbUtils.getCategoriesMapper().deleteByPrimaryKey(productId);
if (productForTest!=null) productsMapper.deleteByPrimaryKey(productForTest.getId());
    }
}
