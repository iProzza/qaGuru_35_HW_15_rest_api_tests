package demoqa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import demoqa.config.ProjectConfiguration;
import demoqa.config.WebConfig;
import demoqa.config.ConfigReader;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import demoqa.helpers.Attach;

import static com.codeborne.selenide.Selenide.closeWebDriver;


public class BaseTest {

    private static final WebConfig config = ConfigReader.getInstance();

    @BeforeAll
    public static void setUpSettings(){
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        ProjectConfiguration projectConfiguration = new ProjectConfiguration(config);
        projectConfiguration.webConfig();
    }

    @AfterEach
    void shutDown() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        // Добавляем логи и видео только для удалённого запуска
        if (Configuration.remote != null) {
            Attach.browserConsoleLogs();
            Attach.addVideo();
        }
        closeWebDriver();
    }
}