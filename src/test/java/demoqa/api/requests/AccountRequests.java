package demoqa.api.requests;


import demoqa.api.models.AuthResponseDto;
import demoqa.api.models.GetAccountUserBooksByIdResponseDto;

import static demoqa.api.EndPoints.ACCOUNT_LOGIN;
import static demoqa.api.EndPoints.ACCOUNT_USER;
import static demoqa.api.specs.CrudResponseSpecs.defaultResponseSpec;
import demoqa.api.specs.DefaultRequestSpec;
import demoqa.config.ConfigReader;
import demoqa.config.WebConfig;
import io.restassured.RestAssured;

import static demoqa.helpers.TestData.LOGIN;
import static demoqa.helpers.TestData.PASSWORD;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class AccountRequests extends DefaultRequestSpec {

    private static final WebConfig config = ConfigReader.getInstance();

    static {
        RestAssured.baseURI = config.baseUrl(); // Устанавливаем базовый URL для ВСЕХ запросов
    }

    //Логинимся через апи
    public static AuthResponseDto authorize() {
        String authData = "{\"userName\":\"" + LOGIN + "\",\"password\":\"" + PASSWORD + "\"}";
        return given()
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
                .extract()
                .as(AuthResponseDto.class);
    }

    public GetAccountUserBooksByIdResponseDto getAccountUserBooksById(String userId) {
        return given()
                .spec(defaultRequestSpec())
                .queryParam("UserId", userId)
                .when()
                .get(ACCOUNT_USER + "/" + userId)
                .then()
                .spec(defaultResponseSpec(200))
                .extract()
                .as(GetAccountUserBooksByIdResponseDto.class);
    }

}
