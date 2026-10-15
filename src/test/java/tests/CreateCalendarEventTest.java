package tests;

import activities.calendar.CalendarEventActivity;
import activities.calendar.CalendarMainActivity;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import sesion.Sesion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CreateCalendarEventTest {

    private static final String EVENT_TITLE = "TAREA SERGIO LISTO";
    private static final String EVENT_DESCRIPTION = "TAREA LISTA";
    private static final Path EVIDENCE_DIRECTORY =
            Path.of("build", "reports", "evidencias");

    private final Sesion sesion = Sesion.getInstance();

    private CalendarMainActivity calendarMainActivity;
    private CalendarEventActivity calendarEventActivity;

    @BeforeEach
    public void setUp() {
        try {
            clearStaleFailureEvidence();
            sesion.getDriver();
            calendarMainActivity = new CalendarMainActivity();
            calendarEventActivity = new CalendarEventActivity();

            calendarMainActivity.waitUntilReady();
            captureScreenshot("01-calendario-abierto.png");
        } catch (RuntimeException exception) {
            captureFailureSafely(exception);
            throw exception;
        }
    }

    @Test
    public void cal001CreateCalendarEvent() throws Throwable {
        try {
            calendarMainActivity.openNewEventForm();
            calendarEventActivity.waitUntilReady();
            calendarEventActivity.enterTitle(EVENT_TITLE);
            calendarEventActivity.enterDescription(EVENT_DESCRIPTION);
            captureScreenshot("02-formulario-completo.png");

            calendarEventActivity.save();
            calendarMainActivity.waitUntilReady();

            String displayedTitle =
                    calendarMainActivity.getVisibleEventTitle(EVENT_TITLE);
            captureScreenshot("03-evento-visible.png");

            Assertions.assertEquals(EVENT_TITLE, displayedTitle);
        } catch (Throwable error) {
            captureFailureSafely(error);
            throw error;
        }
    }

    @AfterEach
    public void tearDown() {
        sesion.closeSession();
    }

    private void captureScreenshot(String fileName) {
        if (!sesion.isActive()) {
            return;
        }

        AppiumDriver driver = sesion.getDriver();
        byte[] screenshot = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES);

        try {
            Files.createDirectories(EVIDENCE_DIRECTORY);
            Files.write(EVIDENCE_DIRECTORY.resolve(fileName), screenshot);
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo guardar la evidencia: " + fileName, exception);
        }
    }

    private void captureFailureSafely(Throwable originalError) {
        try {
            captureScreenshot("error-cal-001.png");
        } catch (RuntimeException screenshotError) {
            originalError.addSuppressed(screenshotError);
        }
    }

    private void clearStaleFailureEvidence() {
        try {
            Files.deleteIfExists(EVIDENCE_DIRECTORY.resolve("error-cal-001.png"));
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo limpiar la evidencia de error anterior", exception);
        }
    }
}
