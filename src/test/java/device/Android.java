package device;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public final class Android implements IDevice {

    @Override
    public AppiumDriver create() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Configuracion del telefono Android conectado, siguiendo la estructura de mobile.
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:automationName", "UiAutomator2");
        capabilities.setCapability("appium:deviceName", "HNJDY-M1");
        capabilities.setCapability("appium:udid", "AETJCP5520404612");
        capabilities.setCapability("appium:platformVersion", "16");

        // Aplicacion Calendario HONOR que se abrira al iniciar la sesion.
        capabilities.setCapability("appium:appPackage", "com.hihonor.calendar");
        capabilities.setCapability(
                "appium:appActivity",
                "com.android.calendar.AllInOneActivity"
        );

        // Conserva los datos y fuerza un inicio controlado de la aplicacion en cada prueba.
        capabilities.setCapability("appium:noReset", true);
        capabilities.setCapability("appium:forceAppLaunch", true);
        capabilities.setCapability("appium:shouldTerminateApp", true);

        try {
            return new AndroidDriver(
                    new URL("http://127.0.0.1:4723/"),
                    capabilities
            );
        } catch (MalformedURLException exception) {
            throw new IllegalStateException("La URL de Appium no es valida", exception);
        }
    }
}
