package activities.calendar;

import control.Button;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import sesion.Sesion;

import java.time.Duration;

public final class CalendarMainActivity {

    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);
    private static final By ADD_EVENT_BUTTON =
            AppiumBy.id("com.hihonor.calendar:id/hnFAB");
    private static final By EVENT_TITLES =
            AppiumBy.id("com.hihonor.calendar:id/item_title");

    private final Button addEventButton = new Button(ADD_EVENT_BUTTON);

    public void waitUntilReady() {
        if (!addEventButton.isDisplayed()) {
            throw new IllegalStateException("La pantalla principal de Calendario no esta lista");
        }
    }

    public void openNewEventForm() {
        addEventButton.click();
    }

    public String getVisibleEventTitle(String expectedTitle) {
        AppiumDriver driver = Sesion.getInstance().getDriver();

        WebElement matchingEvent = new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(currentDriver -> currentDriver.findElements(EVENT_TITLES)
                        .stream()
                        .filter(WebElement::isDisplayed)
                        .filter(element -> expectedTitle.equals(element.getText()))
                        .findFirst()
                        .orElse(null));

        return matchingEvent.getText();
    }
}
