package demoqa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import demoqa.helpers.Attach;

import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class BaseTest {


    @BeforeAll
    static void setup() {
//        WebDriverManager.chromedriver()
//                .clearDriverCache()
//                .clearResolutionCache()
//                .setup();

        Configuration.baseUrl = "https://demoqa.com";
        Configuration.browserSize = System.getProperty("screenResolution", "1920x1080");
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserVersion = System.getProperty("browserVersion", "127.0");
        Configuration.pageLoadStrategy = "eager";
        Configuration.timeout = 10000;

        // Настройки для Selenoid (только если указан selenoid_host)
        configureSelenoidIfNeeded();
    }

    @BeforeEach
    void setUpSettings(){
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
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

    private static void configureSelenoidIfNeeded() {
        String selenoidHost = System.getProperty("selenoid_host");
        if (selenoidHost != null && !selenoidHost.isEmpty()) {
            String selenoidLogin = System.getProperty("selenoid_login", "user1");
            String selenoidPassword = System.getProperty("selenoid_password", "1234");

            Configuration.remote = String.format("https://%s:%s@%s/wd/hub",
                    selenoidLogin,
                    selenoidPassword,
                    selenoidHost);

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("selenoid:options", Map.of(
                    "enableVNC", true,
                    "enableVideo", true
            ));
            Configuration.browserCapabilities = capabilities;
        }
    }
}