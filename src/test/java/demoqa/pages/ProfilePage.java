package demoqa.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {

    private final SelenideElement deleteBtn = $("#delete-record-undefined");


    public void clickDeleteBtn() {
        deleteBtn.click();
    }


}
