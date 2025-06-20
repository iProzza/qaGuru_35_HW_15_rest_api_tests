package api.tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class BaseTest {


    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://demoqa.com";
//        RestAssured.basePath = "/v1";
    }

    @AfterEach
    void shutDown() {
        closeWebDriver();
    }
}