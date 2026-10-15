package activities.calendar;

import control.Button;
import control.TextBox;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import sesion.Sesion;

import java.time.Duration;
import java.util.Map;

public final class CalendarEventActivity {

    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);
    private static final int MAX_SCROLL_ATTEMPTS = 4;

    private static final By TITLE_INPUT =
            AppiumBy.id("com.hihonor.calendar:id/header_title");
    private static final By DESCRIPTION_EDITOR =
            AppiumBy.id("com.hihonor.calendar:id/description_content");
    private static final By SAVE_BUTTON =
            AppiumBy.accessibilityId("Listo");

    private final TextBox titleInput = new TextBox(TITLE_INPUT);
    private final Button saveButton = new Button(SAVE_BUTTON);

    public void waitUntilReady() {
        if (!titleInput.isDisplayed()) {
            throw new IllegalStateException("El formulario de creacion no esta listo");
        }
    }

    public void enterTitle(String title) {
        titleInput.setText(title);
    }

    public void enterDescription(String description) {
        AppiumDriver driver = Sesion.getInstance().getDriver();

        driver.executeScript("mobile: pressKey", Map.of("keycode", 4));
        WebElement descriptionEditor = scrollToDescription(driver);
        descriptionEditor.click();

        // El editor es un WebView sin contexto WEBVIEW. sendKeys() produce
        // InvalidElementStateException; mobile:type escribe sobre el elemento enfocado.
        driver.executeScript("mobile: type", Map.of("text", description));

        new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(currentDriver -> currentDriver.getPageSource().contains(description));
    }

    public void save() {
        saveButton.click();
    }

    private WebElement scrollToDescription(AppiumDriver driver) {
        for (int attempt = 0; attempt < MAX_SCROLL_ATTEMPTS; attempt++) {
            WebElement visibleEditor = findVisibleDescription(driver);
            if (visibleEditor != null) {
                return visibleEditor;
            }

            driver.executeScript("mobile: scrollGesture", Map.of(
                    "left", 50,
                    "top", 350,
                    "width", 620,
                    "height", 900,
                    "direction", "down",
                    "percent", 0.8
            ));
        }

        WebElement visibleEditor = findVisibleDescription(driver);
        if (visibleEditor == null) {
            throw new IllegalStateException("No se encontro el editor de descripcion");
        }
        return visibleEditor;
    }

    private WebElement findVisibleDescription(AppiumDriver driver) {
        return driver.findElements(DESCRIPTION_EDITOR)
                .stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(null);
    }
}
