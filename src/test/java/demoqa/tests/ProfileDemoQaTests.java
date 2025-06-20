package demoqa.tests;

import demoqa.pages.ModalPage;
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
    ModalPage modalPage = new ModalPage();

    @Test
    void deleteBookFromCollectionTest() {
        String authData = "{\"userName\":\"" + login + "\",\"password\":\"" + password + "\"}";

        //Логинимся через апи
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

        //Удаляем все книги из списка
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

        //Добавляем одну книгу isbn
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

        //когда есть книга в списке, картинки этой нет, но я удаляю все книги из списка, но как будто UI это не понимаем, нужна пауза какая-то
        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.path("userId")));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.path("token")));

        //Проверяем, что книга добавилась в список
        open("/profile");
        //Спрятать в класс какой-то, мб проверок каких то
        $(".ReactTable").shouldHave(text("Git Pocket Guide"));


        //Удаляем книгу на UI
        profilePage.clickDeleteBtn();
        modalPage.clickOkBtn();
        System.out.println("Мы тут");

        //Проверяем, что книги нет в списке
        //Спрятать в класс какой-то, мб проверок каких то
        $(".ReactTable").shouldNotHave(text("Git Pocket Guide"));


        //Проверяем, что книга удалилась из списка через API

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .queryParam("UserId", userId)
                .when()
                .get(ACCOUNT_USER + "/" + userId)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();



    }
}
