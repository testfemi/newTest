package enums;

import org.openqa.selenium.Platform;

/**
 * Enum for linking together the available {@link Platform} options.
 */
public enum PlatformOptions {
    MAC(Platform.MAC),
    WINDOWS(Platform.WINDOWS),
    LINUX(Platform.LINUX),
    UNIX(Platform.UNIX);

    private final Platform platform;

    PlatformOptions(Platform platform) {
        this.platform = platform;
    }

    /**
     * Returns the {@link #platform} value for this {@link PlatformOptions}
     * enum.
     *
     * @return the {@link #platform} as a {@link Platform}
     */
    public Platform getPlatform(){
        return platform;
    }

}
