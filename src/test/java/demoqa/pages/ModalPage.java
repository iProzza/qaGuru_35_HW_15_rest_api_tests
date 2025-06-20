package demoqa.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class ModalPage {

    private final SelenideElement okBtnInModal = $("#closeSmallModal-ok");


    public void clickOkBtn() {
        okBtnInModal.click();
    }

}
