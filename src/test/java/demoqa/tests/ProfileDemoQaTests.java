package demoqa.tests;

import demoqa.api.requests.authorization.AuthResponseModel;
import demoqa.api.requests.authorization.AuthorizationApi;
import demoqa.pages.ModalPage;
import demoqa.pages.ProfilePage;
import org.openqa.selenium.Cookie;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static demoqa.api.endpoints.EndPoints.*;
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

    AuthorizationApi authorizationApi = new AuthorizationApi();
    ProfilePage profilePage = new ProfilePage();
    ModalPage modalPage = new ModalPage();

    @Test
    void deleteBookFromCollectionTest() {
        //Авторизуемся через API
        AuthResponseModel authResponseModel = authorizationApi.authorize();

        //Удаляем все книги из списка
        String userId = authResponseModel.getUserId();

        //TODO Чот у метода пустое тело в ответе, а должно вроде быть(судя по сваггеру), в девтулз оно пустое, надо проверить, как будто не удаляет метод книги
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + authResponseModel.getToken())
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
                authResponseModel.getUserId() , isbn);

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + authResponseModel.getToken())
                .body(bookData)
                .when()
                .post(BOOKSTORE_BOOKS)
                .then()
                .log().status()
                .log().body()
                .statusCode(201);

        //когда есть книга в списке, картинки этой нет(но удаление всех книг вроде как происходит), но как будто UI это не понимаем, нужна пауза какая-то
        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", authResponseModel.getUserId()));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponseModel.getExpires()));
        getWebDriver().manage().addCookie(new Cookie("token", authResponseModel.getToken()));

        //Проверяем, что книга добавилась в список
        open("/profile");
        profilePage.bookShouldHaveTitle("Git Pocket Guide");


        //Удаляем книгу на UI
        profilePage.clickDeleteBtn();
        modalPage.clickOkBtn();

        //Проверяем, что книги нет в списке
        profilePage.noRowsFoundMsgIsVisible();


        //Проверяем, что книга удалилась из списка через API

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + authResponseModel.getToken())
                .queryParam("UserId", userId)
                .when()
                .get(ACCOUNT_USER + "/" + userId)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        //Проверка, что книг нет через апи
    }
}
