package demoqa.config;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class ProjectConfiguration {

    private final WebConfig config;

    public ProjectConfiguration(WebConfig config) {
        this.config = config;
    }


    public void webConfig() {

        //TODO вот тут через config все вызвать
        Configuration.baseUrl = "https://demoqa.com";
        Configuration.browserSize = System.getProperty("screenResolution", "1920x1080");
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserVersion = System.getProperty("browserVersion", "127.0");
        Configuration.pageLoadStrategy = "eager";
        Configuration.timeout = 10000;

        // Настройки для Selenoid (только если указан selenoid_host)
        configureSelenoidIfNeeded();
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
