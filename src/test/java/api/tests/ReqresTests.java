package api.tests;

import api.models.SingleUserResponseModel;
import api.models.UserRequestModel;
import api.models.ListUsersResponseModel;
import api.models.CrudUserResponseModel;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static api.specs.UsersSpecs.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


@Tag("all_api")
@Owner("@Azkeww")
public class ReqresTests extends BaseTest {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Получение списка пользователей")
    void usersListTest() {
        ListUsersResponseModel response = step("Получить список пользователей", () ->
                given()
                        .queryParam("page", 2)
                        .when()
                        .get(USERS_PATH)
                        .then()
                        .spec(updateUserResponseSpec200)
                        .extract()
                        .as(ListUsersResponseModel.class));

        step("Проверка ответа", () -> {
            assertThat(response.getPage()).isEqualTo(2);
            assertThat(response.getData())
                    .as("Список пользователей не должен быть пустым")
                    .isNotEmpty();
        });
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Получение данных конкретного пользователя")
    void singleUserTest() {
        SingleUserResponseModel response = step("Получаем пользователя по ID", () ->
                given()
                        .pathParam("id", 2)
                        .when()
                        .get(USER_BY_ID_PATH)
                        .then()
                        .spec(updateUserResponseSpec200)
                        .extract()
                        .as(SingleUserResponseModel.class));

        step("Проверка ответа", () -> {
            assertThat(response.getData().getId()).isEqualTo(2);
            assertThat(response.getData().getEmail())
                    .as("Email должен содержать @ и . символы")
                    .contains("@")
                    .contains(".");
        });
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Создание нового пользователя")
    void createUserTest() {
        UserRequestModel request = step("Подготовока данных для запроса", () ->
                UserRequestModel.builder()
                        .name("morpheus")
                        .job("leader")
                        .build());

        CrudUserResponseModel response = step("Создаем пользователя", () ->
                given(crudUserRequestSpec)
                        .body(request)
                        .when()
                        .post(USERS_PATH)
                        .then()
                        .spec(createUserResponseSpec201)
                        .extract()
                        .as(CrudUserResponseModel.class));

        step("Проверка ответа", () -> {
            assertThat(response.getName()).isEqualTo(request.getName());
            assertThat(response.getJob()).isEqualTo(request.getJob());
            assertThat(response.getId())
                    .as("ID пользователя не должно быть null")
                    .isNotNull();
            assertThat(response.getCreatedAt())
                    .as("Созданная дата должна соответствовать ISO шаблону")
                    .matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z");
        });
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Частичное обновление пользователя (PATCH)")
    void updateUserPatchMethodTest() {
        UserRequestModel request = step("Подготовока данных для запроса", () ->
                UserRequestModel.builder()
                        .name("neo")
                        .job("the one")
                        .build());

        CrudUserResponseModel response = step("Обновляем пользователя методом PATCH", () ->
                given(crudUserRequestSpec)
                        .pathParam("id", 2)
                        .body(request)
                        .when()
                        .patch(USER_BY_ID_PATH)
                        .then()
                        .spec(updateUserResponseSpec200)
                        .extract()
                        .as(CrudUserResponseModel.class));

        step("Проверка ответа", () -> {
            assertThat(response.getName()).isEqualTo(request.getName());
            assertThat(response.getJob()).isEqualTo(request.getJob());
        });
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Полное обновление пользователя (PUT)")
    void updateUserPutMethodTest() {
        UserRequestModel request = step("Подготовока данных для запроса", () ->
                UserRequestModel.builder()
                        .name("neo")
                        .job("the one")
                        .build());

        CrudUserResponseModel response = step("Обновляем пользователя методом PUT", () ->
                given(crudUserRequestSpec)
                        .pathParam("id", 2)
                        .body(request)
                        .when()
                        .put(USER_BY_ID_PATH)
                        .then()
                        .spec(updateUserResponseSpec200)
                        .extract()
                        .as(CrudUserResponseModel.class));

        step("Проверка ответа", () -> {
            assertThat(response.getName()).isEqualTo(request.getName());
            assertThat(response.getJob()).isEqualTo(request.getJob());
        });
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Удаление пользователя")
    void deleteUserTest() {
        step("Удаляем пользователя", () ->
                given(crudUserRequestSpec)
                        .pathParam("id", 2)
                        .when()
                        .delete(USER_BY_ID_PATH)
                        .then()
                        .spec(deleteUserResponseSpec204));
    }
}
