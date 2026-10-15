package sesion;

import device.FactoryDevice;
import io.appium.java_client.AppiumDriver;

public final class Sesion {

    private static final Sesion INSTANCE = new Sesion();

    private AppiumDriver driver;

    private Sesion() {
    }

    public static Sesion getInstance() {
        return INSTANCE;
    }

    public synchronized AppiumDriver getDriver() {
        if (driver == null) {
            driver = FactoryDevice.make("android").create();
        }
        return driver;
    }

    public synchronized boolean isActive() {
        return driver != null && driver.getSessionId() != null;
    }

    public synchronized void closeSession() {
        if (driver == null) {
            return;
        }

        try {
            driver.quit();
        } finally {
            driver = null;
        }
    }
}
