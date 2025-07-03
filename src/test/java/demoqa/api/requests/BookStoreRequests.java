package demoqa.api.requests;

import demoqa.api.specs.DefaultRequestSpec;
import demoqa.config.ConfigReader;
import demoqa.config.WebConfig;
import io.restassured.RestAssured;

import static demoqa.api.EndPoints.BOOKSTORE_BOOKS;
import static demoqa.api.specs.CrudResponseSpecs.defaultResponseSpec;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;

public class BookStoreRequests extends DefaultRequestSpec {

    private static final WebConfig config = ConfigReader.getInstance();

    static {
        RestAssured.baseURI = config.baseUrl(); // Устанавливаем базовый URL для ВСЕХ запросов
    }

    public void addBookToProfile(String userId, String isbn) {
        String bookData = format("{\"userId\":\"%s\",\"collectionOfIsbns\":[{\"isbn\":\"%s\"}]}",
                userId , isbn);
        given()
                .spec(defaultRequestSpec())
                .body(bookData)
                .when()
                .post(BOOKSTORE_BOOKS)
                .then()
                .spec(defaultResponseSpec(201));
    }


    public void deleteAllBooksFromProfileById(String userId) {
        given()
                .spec(defaultRequestSpec())
                .queryParam("UserId", userId)
                .when()
                .delete(BOOKSTORE_BOOKS)
                .then()
                .spec(defaultResponseSpec(204));
    }
}

