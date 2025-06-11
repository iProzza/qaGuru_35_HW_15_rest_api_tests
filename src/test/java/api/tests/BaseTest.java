package api.tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    public static String USERS_PATH = "/users/";
    protected static final String USER_BY_ID_PATH = "/users/{id}";

    protected static final String API_KEY =  "reqres-free-v1";

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }
}