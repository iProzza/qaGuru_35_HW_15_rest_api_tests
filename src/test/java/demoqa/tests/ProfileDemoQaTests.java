package demoqa.tests;

import demoqa.api.models.AuthResponseDto;
import demoqa.api.requests.AccountRequests;
import demoqa.api.models.GetAccountUserBooksByIdResponseDto;
import demoqa.api.requests.BookStoreRequests;
import demoqa.helpers.WithLogin;
import demoqa.pages.ModalPage;
import demoqa.pages.ProfilePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;


public class ProfileDemoQaTests extends BaseTest {

    ProfilePage profilePage = new ProfilePage();
    ModalPage modalPage = new ModalPage();
    BookStoreRequests bookStoreRequests = new BookStoreRequests();
    AccountRequests accountRequests = new AccountRequests();

    @Test
    @WithLogin
    @DisplayName("Проверка отображения пустого списка, после удаления книг")
    void shouldDisplayEmptyBookListAfterDeletionTest() {

        AuthResponseDto authResponseModel = step("Авторизация через API", () ->
                AccountRequests.authorize()
        );


        String userId = authResponseModel.getUserId();

        step("Удаление всех книг в списке", () ->
                bookStoreRequests.deleteAllBooksFromProfileById(userId)
        );

        step("Проверка пустого списка через API", () -> {
            GetAccountUserBooksByIdResponseDto response = accountRequests.getAccountUserBooksById(userId);
            assertThat(response.getBooks()).isEmpty();
        });

        step("Добавление тестовой книги", () ->
                bookStoreRequests.addBookToProfile(userId, "9781449325862")
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
            GetAccountUserBooksByIdResponseDto response = accountRequests.getAccountUserBooksById(userId);
            assertThat(response.getBooks())
                    .as("Проверка, что список книг пуст после удаления")
                    .isEmpty();
        });

    }
}
