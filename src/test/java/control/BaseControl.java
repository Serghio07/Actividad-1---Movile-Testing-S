package control;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import sesion.Sesion;

import java.time.Duration;

public class BaseControl {

    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);

    protected final By locator;

    public BaseControl(By locator) {
        this.locator = locator;
    }

    protected WebElement waitUntilVisible() {
        return new WebDriverWait(Sesion.getInstance().getDriver(), WAIT_TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitUntilClickable() {
        return new WebDriverWait(Sesion.getInstance().getDriver(), WAIT_TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void click() {
        waitUntilClickable().click();
    }

    public String getText() {
        return waitUntilVisible().getText();
    }

    public void setText(String text) {
        WebElement element = waitUntilVisible();
        element.clear();
        element.sendKeys(text);
    }

    public boolean isDisplayed() {
        try {
            return waitUntilVisible().isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }
}
