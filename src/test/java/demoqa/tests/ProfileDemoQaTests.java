package demoqa.tests;

import demoqa.api.requests.authorization.AuthResponseDto;
import demoqa.api.requests.authorization.AuthorizationApi;
import demoqa.api.requests.crud.GetAccountUserBooksByIdResponseDto;
import demoqa.api.requests.crud.Requests;
import demoqa.helpers.WithLogin;
import demoqa.pages.ModalPage;
import demoqa.pages.ProfilePage;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.Cookie;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;


@Tag("all_api")
public class ProfileDemoQaTests extends BaseTest {

    ProfilePage profilePage = new ProfilePage();
    ModalPage modalPage = new ModalPage();
    Requests requests = new Requests();

    //TODO проблема в том, что запрос к логину идет при каждом апи запросе - наверное это неоч!!!!!
    //TODO сделать steps
    //TODO убрать bookData внутрь addBookToProfile, а через аргуменнты передавать isbn и userId

    @Test
    @WithLogin
    @DisplayName("Проверка отображения пустого списка, после удаления книг")
    void shouldDisplayEmptyBookListAfterDeletionTest() {
        //Авторизуемся через API
        AuthResponseDto authResponseModel = AuthorizationApi.authorize();

        //Удаляем все книги из списка
        String userId = authResponseModel.getUserId();
        requests.deleteAllBooksFromProfileById(userId);


        //Добавляем одну книгу isbn
        String isbn = "9781449325862";
        String bookData = format("{\"userId\":\"%s\",\"collectionOfIsbns\":[{\"isbn\":\"%s\"}]}",
                userId , isbn);
        requests.addBookToProfile(bookData);

        //Проверяем, что книга добавилась в список
        open("/profile");
        profilePage.bookShouldHaveTitle("Git Pocket Guide");

        //Удаляем книгу на UI
        profilePage.clickDeleteBtn();
        modalPage.clickOkBtn();

        //Проверяем, что книги нет в списке UI
        profilePage.noRowsFoundMsgIsVisible();

        //Проверяем, что книги нет в списке API
        GetAccountUserBooksByIdResponseDto response = requests.getAccountUserBooksById(userId);
        assertThat(response.getBooks())
                .as("Проверка, что список книг пуст")
                .isEmpty();

    }
}
