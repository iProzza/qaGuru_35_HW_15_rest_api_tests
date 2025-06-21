package demoqa.api.requests.authorization;


import static demoqa.api.endpoints.EndPoints.ACCOUNT_LOGIN;
import static demoqa.helpers.TestData.LOGIN;
import static demoqa.helpers.TestData.PASSWORD;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class AuthorizationApi {

    //Логинимся через апи
    public static AuthResponseModel authorization() {
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
                .as(AuthResponseModel.class);
    }
}
