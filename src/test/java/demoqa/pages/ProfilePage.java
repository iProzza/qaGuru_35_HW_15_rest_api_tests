package demoqa.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {

    private final SelenideElement deleteBtn = $("#delete-record-undefined");
    private final SelenideElement bookTitle = $(".mr-2");
    private final SelenideElement noRowsFoundMsg = $(".rt-noData");


    public void clickDeleteBtn() {
        deleteBtn.click();
    }

    public void bookShouldHaveTitle(String title) {
        bookTitle.shouldHave(text(title));
    }

    public void noRowsFoundMsgIsVisible() {
        noRowsFoundMsg.shouldHave(text("No rows found"));
    }

}
