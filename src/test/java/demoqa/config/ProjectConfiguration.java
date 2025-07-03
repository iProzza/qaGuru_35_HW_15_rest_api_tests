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

        Configuration.baseUrl = config.baseUrl();
        Configuration.browserSize = config.browserSize();
        Configuration.browser = config.browser().name().toLowerCase();
        Configuration.browserVersion = config.browserVersion();
        Configuration.pageLoadStrategy = "eager";

        if (config.isRemote()) {
            configureRemote();
        }
    }

    private void configureRemote() {
        Configuration.remote = config.remoteUrl();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.of(
                "enableVNC", true,
                "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;
    }
}
