package demoqa.tests;

import demoqa.pages.ProfilePage;
import org.openqa.selenium.Cookie;
import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static demoqa.helpers.TestData.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;


@Tag("all_api")
public class ProfileDemoQaTests extends BaseTest {

    ProfilePage profilePage = new ProfilePage();

    @Test
    void deleteBookFromCollectionTest() {
        String authData = "{\"userName\":\"" + login + "\",\"password\":\"" + password + "\"}";

        Response authResponse = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(authData)
                .when()
                .post(ACCOUNT_LOGIN)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        String userId = authResponse.path("userId");

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .queryParam("UserId", userId)
                .when()
                .delete(BOOKSTORE_BOOKS)
                .then()
                .log().status()
                .log().body()
                .statusCode(204);

        String isbn = "9781449325862";
        String bookData = format("{\"userId\":\"%s\",\"collectionOfIsbns\":[{\"isbn\":\"%s\"}]}",
                authResponse.path("userId") , isbn);

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(bookData)
                .when()
                .post(BOOKSTORE_BOOKS)
                .then()
                .log().status()
                .log().body()
                .statusCode(201);

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.path("userId")));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.path("token")));

        open("/profile");
        $(".ReactTable").shouldHave(text("Git Pocket Guide"));


        //Удаляем книгу на UI
        profilePage.clickDeleteBtn();


    }




//    @Test
//    @DisplayName("Получение списка пользователей")
//    void usersListTest() {
//        ListUsersResponseModel response = step("Get users list", () ->
//                given()
//                        .queryParam("page", 2)
//                        .when()
//                        .get(USERS_PATH)
//                        .then()
//                        .spec(updateUserResponseSpec200)
//                        .extract()
//                        .as(ListUsersResponseModel.class));
//
//        step("Check response data", () -> {
//            assertThat(response.getPage()).isEqualTo(2);
//            assertThat(response.getData())
//                    .as("User list should not be empty")
//                    .isNotEmpty();
//        });
//    }
//
//    @Test
//    @DisplayName("Получение данных конкретного пользователя")
//    void singleUserTest() {
//        SingleUserResponseModel response = step("Get user by ID", () ->
//                given()
//                        .header("x-api-key", API_KEY)
//                        .pathParam("id", 2)
//                        .when()
//                        .get(USER_BY_ID_PATH)
//                        .then()
//                        .spec(updateUserResponseSpec200)
//                        .extract()
//                        .as(SingleUserResponseModel.class));
//
//        step("Check user data", () -> {
//            assertThat(response.getData().getId()).isEqualTo(2);
//            assertThat(response.getData().getEmail())
//                    .as("Email should contain @ and . symbols")
//                    .contains("@")
//                    .contains(".");
//        });
//    }
//
//    @Test
//    @DisplayName("Создание нового пользователя")
//    void createUserTest() {
//        UserRequestModel request = step("Prepare request data", () ->
//                UserRequestModel.builder()
//                        .name("morpheus")
//                        .job("leader")
//                        .build());
//
//        crudUserResponseModel response = step("Create user", () ->
//                given(crudUserRequestSpec)
//                        .body(request)
//                        .when()
//                        .post(USERS_PATH)
//                        .then()
//                        .spec(createUserResponseSpec201)
//                        .extract()
//                        .as(crudUserResponseModel.class));
//
//        step("Verify response", () -> {
//            assertThat(response.getName()).isEqualTo(request.getName());
//            assertThat(response.getJob()).isEqualTo(request.getJob());
//            assertThat(response.getId())
//                    .as("User ID should not be null")
//                    .isNotNull();
//            assertThat(response.getCreatedAt())
//                    .as("Creation date should follow ISO pattern")
//                    .matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z");
//        });
//    }
//
//    @Test
//    @DisplayName("Частичное обновление пользователя (PATCH)")
//    void updateUserPatchMethodTest() {
//        UserRequestModel request = step("Prepare update data", () ->
//                UserRequestModel.builder()
//                        .name("neo")
//                        .job("the one")
//                        .build());
//
//        crudUserResponseModel response = step("Update user with PATCH", () ->
//                given(crudUserRequestSpec)
//                        .pathParam("id", 2)
//                        .body(request)
//                        .when()
//                        .patch(USER_BY_ID_PATH)
//                        .then()
//                        .spec(updateUserResponseSpec200)
//                        .extract()
//                        .as(crudUserResponseModel.class));
//
//        step("Verify update results", () -> {
//            assertThat(response.getName()).isEqualTo(request.getName());
//            assertThat(response.getJob()).isEqualTo(request.getJob());
//        });
//    }
//
//    @Test
//    @DisplayName("Полное обновление пользователя (PUT)")
//    void updateUserPutMethodTest() {
//        UserRequestModel request = step("Prepare update data", () ->
//                UserRequestModel.builder()
//                        .name("neo")
//                        .job("the one")
//                        .build());
//
//        crudUserResponseModel response = step("Update user with PUT", () ->
//                given(crudUserRequestSpec)
//                        .pathParam("id", 2)
//                        .body(request)
//                        .when()
//                        .put(USER_BY_ID_PATH)
//                        .then()
//                        .spec(updateUserResponseSpec200)
//                        .extract()
//                        .as(crudUserResponseModel.class));
//
//        step("Verify update results", () -> {
//            assertThat(response.getName()).isEqualTo(request.getName());
//            assertThat(response.getJob()).isEqualTo(request.getJob());
//        });
//    }
//
//    @Test
//    @DisplayName("Удаление пользователя")
//    void deleteUserTest() {
//        step("Delete user", () ->
//                given(crudUserRequestSpec)
//                        .pathParam("id", 2)
//                        .when()
//                        .delete(USER_BY_ID_PATH)
//                        .then()
//                        .spec(deleteUserResponseSpec204));
//    }
}
