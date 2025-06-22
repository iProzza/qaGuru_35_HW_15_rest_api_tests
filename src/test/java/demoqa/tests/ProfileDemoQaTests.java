package demoqa.tests;

import demoqa.api.requests.authorization.AuthResponseDto;
import demoqa.api.requests.authorization.AuthorizationApi;
import demoqa.api.requests.crud.GetAccountUserBooksByIdResponseDto;
import demoqa.api.requests.crud.Requests;
import demoqa.helpers.WithLogin;
import demoqa.pages.ModalPage;
import demoqa.pages.ProfilePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;


@Tag("all_api")
public class ProfileDemoQaTests extends BaseTest {

    ProfilePage profilePage = new ProfilePage();
    ModalPage modalPage = new ModalPage();
    Requests requests = new Requests();

    @Test
    @WithLogin
    @DisplayName("Проверка отображения пустого списка, после удаления книг")
    void shouldDisplayEmptyBookListAfterDeletionTest() {

        AuthResponseDto authResponseModel = step("Авторизация через API", () ->
                AuthorizationApi.authorize()
        );


        String userId = authResponseModel.getUserId();

        step("Удаление всех книг в списке", () ->
                requests.deleteAllBooksFromProfileById(userId)
        );

        step("Проверка пустого списка через API", () -> {
            GetAccountUserBooksByIdResponseDto response = requests.getAccountUserBooksById(userId);
            assertThat(response.getBooks()).isEmpty();
        });

        step("Добавление тестовой книги", () ->
                requests.addBookToProfile(userId, "9781449325862")
        );

        step("Открытие страницы профиля", () ->
                open("/profile")
        );

        step("Проверка отображения книги", () ->
                profilePage.bookShouldHaveTitle("Git Pocket Guide")
        );

        step("Удаление книги через UI", () -> {
            profilePage.clickDeleteBtn();
            modalPage.clickOkBtn();
        });

        step("Проверка пустого списка в UI", () ->
                profilePage.noRowsFoundMsgIsVisible()
        );

        step("Проверка пустого списка через API", () -> {
            GetAccountUserBooksByIdResponseDto response = requests.getAccountUserBooksById(userId);
            assertThat(response.getBooks())
                    .as("Проверка, что список книг пуст после удаления")
                    .isEmpty();
        });

    }
}
