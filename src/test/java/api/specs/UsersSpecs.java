package api.specs;

import api.tests.BaseTest;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static api.helpers.CustomAllureListener.withCustomTemplates;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;

public class UsersSpecs extends BaseTest {

    public static RequestSpecification crudUserRequestSpec = with()
            .filter(withCustomTemplates())
            .log().all()
            .header("x-api-key", API_KEY)
            .contentType(JSON);

    public static ResponseSpecification createUserResponseSpec201 = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .log(STATUS)
            .log(BODY)
            .build();

    public static ResponseSpecification updateUserResponseSpec200 = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(STATUS)
            .log(BODY)
            .build();

    public static ResponseSpecification deleteUserResponseSpec204 = new ResponseSpecBuilder()
            .expectStatusCode(204)
            .log(STATUS)
            .build();

}
