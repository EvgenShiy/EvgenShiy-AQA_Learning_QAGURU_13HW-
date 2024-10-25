package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {


    @BeforeAll
    public static void setUp() {

        String browserSize = System.getProperty("browserSize", "1920x1080");
        String browserName = System.getProperty("browser", "chrome");
        String browserVersion = System.getProperty("browserVersion", "116");
        String remoteDriverURL = System.getProperty("remoteDriverURL", "https://user1:1234@selenoid.autotests.cloud/wd/hub");

        Configuration.remote = remoteDriverURL;
        Configuration.browser = browserName;
        Configuration.browserSize = browserSize;  // Устанавливаем размер окна браузера
        Configuration.browserVersion = browserVersion;
        Configuration.pageLoadStrategy = "eager"; // Оптимизация загрузки страницы
        Configuration.holdBrowserOpen = false;    // Использовать true для отладки
        Configuration.baseUrl = "https://demoqa.com"; // Базовый URL

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    public void afterEach() {
        addAttachments();  // Добавление вложений (скриншоты, логи, видео)
        closeWebDriver();  // Закрытие браузера после каждого теста
    }

    public void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
    }
}
