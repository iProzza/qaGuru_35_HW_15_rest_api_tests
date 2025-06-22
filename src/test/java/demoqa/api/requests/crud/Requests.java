package demoqa.api.requests.crud;

import demoqa.api.specs.DefaultRequestSpec;

import static demoqa.api.endpoints.EndPoints.ACCOUNT_USER;
import static demoqa.api.endpoints.EndPoints.BOOKSTORE_BOOKS;
import static demoqa.api.specs.CrudResponseSpecs.*;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;

public class Requests extends DefaultRequestSpec {

    public void addBookToProfile(String userId, String isbn) {
        String bookData = format("{\"userId\":\"%s\",\"collectionOfIsbns\":[{\"isbn\":\"%s\"}]}",
                userId , isbn);
        given()
                .spec(defaultRequestSpec())
                .body(bookData)
                .when()
                .post(BOOKSTORE_BOOKS)
                .then()
                .spec(addBookToProfileResponseSpec201);
    }


    public void deleteAllBooksFromProfileById(String userId) {
        given()
                .spec(defaultRequestSpec())
                .queryParam("UserId", userId)
                .when()
                .delete(BOOKSTORE_BOOKS)
                .then()
                .spec(deleteAllBooksFromProfileByIdResponseSpec204);
    }

    public GetAccountUserBooksByIdResponseDto getAccountUserBooksById(String userId) {
        return given()
                .spec(defaultRequestSpec())
                .queryParam("UserId", userId)
                .when()
                .get(ACCOUNT_USER + "/" + userId)
                .then()
                .spec(getAccountUserBooksByIdResponseSpec200)
                .extract()
                .as(GetAccountUserBooksByIdResponseDto.class);
    }

}

