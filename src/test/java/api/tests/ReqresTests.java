package api.tests;

import api.models.UserResponse;
import api.models.UserRequest;
import api.models.UsersListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import io.restassured.http.ContentType;

public class ReqresTests extends BaseTest {

    @Test
    @DisplayName("Получение списка пользователей")
    void usersListTest() {
        UsersListResponse response = step("Get users list", () ->
                given()
                        .queryParam("page", 2)
                        .when()
                        .get(USERS_PATH)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(UsersListResponse.class));

        step("Check response data", () -> {
            assertThat(response.getPage()).isEqualTo(2);
            assertThat(response.getData())
                    .as("User list should not be empty")
                    .isNotEmpty();

            UserResponse firstUser = response.getData().get(0);
            assertThat(firstUser.getEmail())
                    .as("User email should not be null")
                    .isNotNull();
        });
    }

    @Test
    @DisplayName("Получение данных конкретного пользователя")
    void singleUserTest() {
        UserResponse user = step("Get user by ID", () ->
                given()
                        .header("x-api-key", API_KEY)
                        .pathParam("id", 2)
                        .when()
                        .get(USER_BY_ID_PATH)
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getObject("data", UserResponse.class));

        step("Check user data", () -> {
            assertThat(user.getId()).isEqualTo(2);
            assertThat(user.getEmail())
                    .as("Email should contain @ and . symbols")
                    .contains("@")
                    .contains(".");
            assertThat(user.getFirst_name()).isNotBlank();
        });
    }

    @Test
    @DisplayName("Создание нового пользователя")
    void createUserTest() {
        UserRequest userRequest = step("Prepare request data", () ->
                UserRequest.builder()
                        .name("morpheus")
                        .job("leader")
                        .build());

        UsersListResponse response = step("Create user", () ->
                given()
                        .header("x-api-key", API_KEY)
                        .contentType(ContentType.JSON)
                        .body(userRequest)
                        .when()
                        .post(USERS_PATH)
                        .then()
                        .statusCode(201)
                        .extract()
                        .as(UsersListResponse.class));

        step("Verify response", () -> {
            assertThat(response.getName()).isEqualTo(userRequest.getName());
            assertThat(response.getJob()).isEqualTo(userRequest.getJob());
            assertThat(response.getId())
                    .as("User ID should not be null")
                    .isNotNull();
            assertThat(response.getCreatedAt())
                    .as("Creation date should follow ISO pattern")
                    .matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z");
        });
    }

    @Test
    @DisplayName("Частичное обновление пользователя (PATCH)")
    void updateUserPatchMethodTest() {
        UserRequest updateData = step("Prepare update data", () ->
                UserRequest.builder()
                        .name("neo")
                        .job("the one")
                        .build());

        UsersListResponse response = step("Update user with PATCH", () ->
                given()
                        .header("x-api-key", API_KEY)
                        .contentType(ContentType.JSON)
                        .pathParam("id", 2)
                        .body(updateData)
                        .when()
                        .patch(USER_BY_ID_PATH)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(UsersListResponse.class));

        step("Verify update results", () -> {
            assertThat(response.getName()).isEqualTo(updateData.getName());
            assertThat(response.getJob()).isEqualTo(updateData.getJob());
        });
    }

    @Test
    @DisplayName("Полное обновление пользователя (PUT)")
    void updateUserPutMethodTest() {
        UserRequest updateData = step("Prepare update data", () ->
                UserRequest.builder()
                        .name("neo")
                        .job("the one")
                        .build());

        UsersListResponse response = step("Update user with PUT", () ->
                given()
                        .header("x-api-key", API_KEY)
                        .contentType(ContentType.JSON)
                        .pathParam("id", 2)
                        .body(updateData)
                        .when()
                        .put(USER_BY_ID_PATH)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(UsersListResponse.class));

        step("Verify update results", () -> {
            assertThat(response.getName()).isEqualTo(updateData.getName());
            assertThat(response.getJob()).isEqualTo(updateData.getJob());
        });
    }

    @Test
    @DisplayName("Удаление пользователя")
    void deleteUserTest() {
        step("Delete user", () ->
                given()
                        .header("x-api-key", API_KEY)
                        .pathParam("id", 2)
                        .when()
                        .delete(USER_BY_ID_PATH)
                        .then()
                        .statusCode(204));
    }
}
