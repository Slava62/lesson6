package ru.slava62;

import static io.qameta.allure.Allure.step;
import com.github.javafaker.Faker;;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.*;
import retrofit2.Converter;
import retrofit2.Response;
import ru.slava62.db.dao.CategoriesMapper;
import ru.slava62.db.model.Categories;
import ru.slava62.dto.Category;
import ru.slava62.dto.ErrorBody;
import ru.slava62.dto.Product;
import ru.slava62.service.CategoryService;
import ru.slava62.util.DbUtils;
import ru.slava62.util.RetrofitUtils;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Market tests")
@Feature("Category test-suite")
public class CategoryTests {

   static CategoryService categoryService;
   static Categories categoryForTest;
   static CategoriesMapper categoriesMapper;
   Faker faker = new Faker();
    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        categoriesMapper=DbUtils.getCategoriesMapper();
        categoryService = RetrofitUtils.getCategoryService();
        categoryForTest=null;
    }

    @BeforeEach
    void setUp() {
        categoryForTest=new Categories();
        categoryForTest.setTitle(faker.food().ingredient());
        categoriesMapper.insert(categoryForTest);
    }

    @SneakyThrows
    @Test
    @DisplayName("New category creation test")
    @Description("Check that the category added to the database is available")
    void getNewCategoryFromDBTest()  {
        Response<Category> response=RetrofitUtils.getCategoryResponse(categoryForTest.getId(), categoryService);
        step("Check successful response");
        assertThat(response.isSuccessful(),is(true));
        step("Check category was added in DB");
        assertThat(categoryForTest.getId(),equalTo(response.body().getId()));
        step("Check title of category");
        assertThat(categoryForTest.getTitle(),equalTo(response.body().getTitle()));
    }
    @SneakyThrows
    @Test
    @DisplayName("Update category negative test")
    @Description("Checking that the category update method is not allowed by using API")
    void updateNewCategoryTest()  {
        categoryForTest.setTitle("test_updated_category");
        Response response=RetrofitUtils.updateCategoryResponse(categoryForTest.getId(), categoryService);
        step("Check response code 405");
        assertThat(response.code(),is(405));
        step("Check response error is \"Method Not Allowed\"");
        assertThat(ErrorBody.getErorrMessage(response),is(equalTo("Method Not Allowed")));

    }
    @SneakyThrows
    @Test
    @DisplayName("Delete category negative test")
    @Description("Checking that the category delete method is not allowed by using API")
    void deleteNewCategoryTest()  {
        Response response=RetrofitUtils.deleteCategoryResponse(categoryForTest.getId(), categoryService);
        step("Check response code 405");
        assertThat(response.code(),is(405));
        step("Check response error is \"Method Not Allowed\"");
        assertThat(ErrorBody.getErorrMessage(response),is(equalTo("Method Not Allowed")));
    }
    @SneakyThrows
    @Test
    @DisplayName("Create category negative test")
    @Description("Checking that the method of adding a category using the API is not implemented")
    void createNewCategoryTest()  {
        Response response=RetrofitUtils.createCategoryResponse
                (new Category(categoryForTest.getId(), categoryForTest.getTitle()
                        ,new ArrayList<Product>()), categoryService);
        step("Check response code 404");
        assertThat(response.code(),is(404));
        step("Check response error is \"Not Found\"");
        assertThat(ErrorBody.getErorrMessage(response),is(equalTo("Not Found")));
    }
    @AfterEach
    void tearDown() {
        //delete category from base
        if (categoryForTest!=null) categoriesMapper.deleteByPrimaryKey(categoryForTest.getId());
    }

    @AfterAll
    static void afterAll() {

    }


}
