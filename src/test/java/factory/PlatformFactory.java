package factory;

import enums.PlatformOptions;
import helper.ScenarioContext;
import helper.Log;
import helper.TestConfig;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Utility class for configuring what OS/device Platform capabilities are needed for the
 * WebDriver instance.
 */
public class PlatformFactory {

    /**
     * Based on the "platform" property-value configured in the {@link TestConfig}, apply the
     * applicable set of pre-configured capabilities hardcoded into this method for configuring
     * the platform of the {@link WebDriver} instance.
     * <p>
     * @param caps the {@link DesiredCapabilities} object to set capabilities for
     * @return the {@link DesiredCapabilities} object with the applicable Platform capabilities
     * set on it.
     * @see CapabilityType documentation for further details on what available capabilites can
     * be configured.
     */
    public static DesiredCapabilities initialisePlatform(DesiredCapabilities caps) {
        /* Retrieve the "platform" property-value specified from the TestConfig (i.e. from the applicable ".properties"
        file) and save it to the ExecutionContext */
        ScenarioContext.platform = PlatformOptions.valueOf(TestConfig.getPropertyValue("platform").toUpperCase());
        switch (ScenarioContext.platform.getPlatform()) {
            case WINDOWS -> {
                Log.info("Setting Windows Platform Capabilities:");
                caps.setPlatform(PlatformOptions.WINDOWS.getPlatform());
                Log.debug("SETTING PLATFORM CAPABILITY: Perform Execution locally on Windows platform");
                Log.debug("SETTING PLATFORM CAPABILITY: Accept all SSL certificates (including insecure certificates) by default");
            }
            case LINUX -> {
                Log.info("Setting Linux Platform Capabilities:");
                caps.setPlatform(PlatformOptions.LINUX.getPlatform());
                Log.debug("SETTING PLATFORM CAPABILITY: Perform Execution locally on Linux platform");
                Log.debug("SETTING PLATFORM CAPABILITY: Accept all SSL certificates (including insecure certificates) by default");
            }
            case MAC -> {
                Log.info("Setting Mac Platform Capabilities:");
                caps.setPlatform(PlatformOptions.MAC.getPlatform());
                Log.debug("SETTING PLATFORM CAPABILITY: Perform Execution locally on Mac platform");
            }
            //TODO - Implement Mac capabilities (if needed)
            case UNIX -> {
                Log.info("Setting Unix Platform Capabilities:");
                caps.setPlatform(PlatformOptions.UNIX.getPlatform());
                Log.debug("SETTING PLATFORM CAPABILITY: Perform Execution locally on Unix platform");
            }
            //TODO - Implement Unix capabilities (if needed)
            default -> {
                Log.warn("Platform Not Specified");
                Log.info("Setting Default Platform Capabilities:");
                caps.setPlatform(Platform.ANY);
            }
            //TODO - Implement default capabilities (if needed)
        }
        return caps;
    }

}
