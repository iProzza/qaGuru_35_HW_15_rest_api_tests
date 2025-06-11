package api.tests;

import api.models.SingleUserResponseModel;
import api.models.UserRequestModel;
import api.models.ListUsersResponseModel;
import api.models.crudUserResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static api.specs.UsersSpecs.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ReqresTests extends BaseTest {

    @Test
    @DisplayName("Получение списка пользователей")
    void usersListTest() {
        ListUsersResponseModel response = step("Get users list", () ->
                given()
                        .queryParam("page", 2)
                        .when()
                        .get(USERS_PATH)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(ListUsersResponseModel.class));

        step("Check response data", () -> {
            assertThat(response.getPage()).isEqualTo(2);
            assertThat(response.getData())
                    .as("User list should not be empty")
                    .isNotEmpty();
        });
    }

    @Test
    @DisplayName("Получение данных конкретного пользователя")
    void singleUserTest() {
        SingleUserResponseModel response = step("Get user by ID", () ->
                given()
                        .header("x-api-key", API_KEY)
                        .pathParam("id", 2)
                        .when()
                        .get(USER_BY_ID_PATH)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(SingleUserResponseModel.class));

        step("Check user data", () -> {
            assertThat(response.getData().getId()).isEqualTo(2);
            assertThat(response.getData().getEmail())
                    .as("Email should contain @ and . symbols")
                    .contains("@")
                    .contains(".");
        });
    }

    @Test
    @DisplayName("Создание нового пользователя")
    void createUserTest() {
        UserRequestModel request = step("Prepare request data", () ->
                UserRequestModel.builder()
                        .name("morpheus")
                        .job("leader")
                        .build());

        crudUserResponseModel response = step("Create user", () ->
                given(crudUserRequestSpec)
                        .body(request)
                        .when()
                        .post(USERS_PATH)
                        .then()
                        .spec(createUserResponseSpec)
                        .extract()
                        .as(crudUserResponseModel.class));

        step("Verify response", () -> {
            assertThat(response.getName()).isEqualTo(request.getName());
            assertThat(response.getJob()).isEqualTo(request.getJob());
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
        UserRequestModel request = step("Prepare update data", () ->
                UserRequestModel.builder()
                        .name("neo")
                        .job("the one")
                        .build());

        crudUserResponseModel response = step("Update user with PATCH", () ->
                given(crudUserRequestSpec)
                        .pathParam("id", 2)
                        .body(request)
                        .when()
                        .patch(USER_BY_ID_PATH)
                        .then()
                        .spec(updateUserResponseSpec)
                        .extract()
                        .as(crudUserResponseModel.class));

        step("Verify update results", () -> {
            assertThat(response.getName()).isEqualTo(request.getName());
            assertThat(response.getJob()).isEqualTo(request.getJob());
        });
    }

    @Test
    @DisplayName("Полное обновление пользователя (PUT)")
    void updateUserPutMethodTest() {
        UserRequestModel request = step("Prepare update data", () ->
                UserRequestModel.builder()
                        .name("neo")
                        .job("the one")
                        .build());

        crudUserResponseModel response = step("Update user with PUT", () ->
                given(crudUserRequestSpec)
                        .pathParam("id", 2)
                        .body(request)
                        .when()
                        .put(USER_BY_ID_PATH)
                        .then()
                        .spec(updateUserResponseSpec)
                        .extract()
                        .as(crudUserResponseModel.class));

        step("Verify update results", () -> {
            assertThat(response.getName()).isEqualTo(request.getName());
            assertThat(response.getJob()).isEqualTo(request.getJob());
        });
    }

    @Test
    @DisplayName("Удаление пользователя")
    void deleteUserTest() {
        step("Delete user", () ->
                given(crudUserRequestSpec)
                        .pathParam("id", 2)
                        .when()
                        .delete(USER_BY_ID_PATH)
                        .then()
                        .spec(deleteUserResponseSpec));
    }
}
