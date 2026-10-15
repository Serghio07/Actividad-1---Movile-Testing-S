package device;

import java.util.Locale;

public final class FactoryDevice {

    private FactoryDevice() {
    }

    public static IDevice make(String deviceType) {
        if (deviceType == null || deviceType.isBlank()) {
            throw new IllegalArgumentException("El tipo de dispositivo es obligatorio");
        }

        return switch (deviceType.trim().toLowerCase(Locale.ROOT)) {
            case "android" -> new Android();
            default -> throw new IllegalArgumentException(
                    "Tipo de dispositivo no soportado: " + deviceType
            );
        };
    }
}
