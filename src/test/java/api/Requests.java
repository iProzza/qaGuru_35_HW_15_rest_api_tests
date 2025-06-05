package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Requests extends BaseTest {
    public static Response getUsers(int page) {
        return given()
                .queryParam("page", page)
                .when()
                .get(USERS_PATH);
    }

    public static Response getUser(int userId) {
        return given()
                .header("x-api-key", API_KEY)
                .pathParam("id", userId)
                .when()
                .get(USER_BY_ID_PATH);
    }

    public static Response createUser(String name, String job) {
        return given()
                .header("x-api-key", API_KEY)
                .contentType(ContentType.JSON)
                .body(String.format("{\"name\": \"%s\", \"job\": \"%s\"}", name, job))
                .when()
                .post(USERS_PATH);
    }

    public static Response updateUser(int userId, String name, String job) {
        return given()
                .header("x-api-key", API_KEY)
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .body(String.format("{\"name\": \"%s\", \"job\": \"%s\"}", name, job))
                .when()
                .patch(USER_BY_ID_PATH);
    }

    public static Response deleteUser(int userId) {
        return given()
                .header("x-api-key", API_KEY)
                .pathParam("id", userId)
                .when()
                .delete(USER_BY_ID_PATH);
    }
}
