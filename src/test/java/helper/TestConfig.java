package helper;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import utility.AESUtil;
import utility.DateHandler;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Properties;

/**
 * Utility for loading ".properties" files and configurations for the current scenario under execution
 * (SUT) on a per-environment basis.  If no configuration is passed, then DEFAULT configurations will
 * be applied for execution.
 */
public class TestConfig {

    private static final CompositeConfiguration
            config = new CompositeConfiguration(); // To store all property configurations
    public static final String
            DEFAULT_CONFIGURATION_FILENAME = "config.properties", // Filename of the default configuration file
            TEST_CONFIG_FILENAME, // Filename of the test configuration file
            TEST_ENVIRONMENT;

    /* Perform the following operations when this class is first loaded into the JVM/memory during runtime */
    static {
        // Load default configs
        loadConfiguration(DEFAULT_CONFIGURATION_FILENAME);
        Log.debug("Default Configurations Loaded from %s", DEFAULT_CONFIGURATION_FILENAME);

        /* Identify which test environment ".properties" file to load (NOTE: If the "env" property
        is NOT set in the DEFAULT_CONFIGURATION_FILENAME, then the DEFAULT_TEST_ENVIRONMENT file
        will be used */
        try {
            TEST_ENVIRONMENT = getPropertyValue("test_env");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        // Load the applicable config file for the TEST_ENVIRONMENT
        TEST_CONFIG_FILENAME = TEST_ENVIRONMENT + ".properties";
        loadConfiguration(TEST_CONFIG_FILENAME);
        Log.debug("Additional Test Configurations Loaded from %s", TEST_CONFIG_FILENAME);

        // Save execution start time (get time from DEFAULT_CONFIGURATION_FILENAME)
        try {
            ScenarioContext.executionStartTime = ScenarioContext.executionStartTime == null
                    ? DateHandler.convertToDateObject(
                        TestConfig.getPropertyValue("maven_execution_start_timestamp"),
                        TestConfig.getPropertyValue("maven_execution_timestamp_format"))
                    : ScenarioContext.executionStartTime;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves the property configurations, located from the file identified by the
     * {@param resourceFileName}, to the {@link #config}.
     *
     * @param resourceFileName the name and format/extension of the resource file.
     * @throws NullPointerException if {@param resourceFileName} is null.
     * @throws ExceptionInInitializerError if {@param resourceFileName} cannot be
     * found/loaded successfully
     */
    public static void loadConfiguration(@NotNull String resourceFileName) {
        Objects.requireNonNull(resourceFileName, "The resource file name provided cannot be null");
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(new Parameters().properties()
                                .setListDelimiterHandler(new DefaultListDelimiterHandler(','))
                                .setFileName(resourceFileName));
        Log.debug("Loading Property Configurations from %s", resourceFileName);
        try {
            config.addConfiguration(builder.getConfiguration());
        } catch (ConfigurationException e) {
            Log.error("Failed to load configuration from %s", resourceFileName);
            throw new ExceptionInInitializerError(e);
        }

        //Apply configurations to System properties as well
        Iterator<String> it = config.getKeys();
        while(it.hasNext()){
            String key = it.next();
            System.setProperty(key, config.getString(key));
        }
    }

    /**
     * Save a new key-value property, with the key {@param key} and value of {@param value},
     * to the {@link #config} and to the local system properties, by implicitly calling
     * {@link System#setProperty(String, String)}.
     *
     * @param key the key for referencing the new property
     * @param val the value to assign this new property
     */
    public static void setPropertyValue(String key, String val){
        config.setProperty(key, val);
        System.setProperty(key, val);
    }

    /**
     * Retrieves the value currently set for the property with the name/key equal
     * to {@param propertyName}.
     *
     * @param propertyName the property name to retrieve.
     * @return String the value assigned to this property
     * @throws NullPointerException if no value for {@param propertyName} is set/can
     * be found
     */
    public static @NotNull String getPropertyValue(String propertyName) {
        String value = config.getString(propertyName);
        // If value is encrypted, attempt to decrypt it
        if (AESUtil.isEncrypted(value)) {
            try {
                String cleaned = AESUtil.removeEncryptionMarkers(value);
                value = AESUtil.decrypt(cleaned);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        if(value == null){
            throw new NullPointerException("Could not find the '" + propertyName + "' property.  Check if this has been properly configured");
        }
        return value;
    }

    /**
     * Retrieves the values currently set for the property with the name/key equal
     * to {@param propertyName}.
     *
     * @param propertyName the property name to retrieve.
     * @return String[] the values assigned to {@param propertyName}.
     * @throws NullPointerException if no value for {@param propertyName} is set/can
     * be found
     */
    public static String @NotNull [] getPropertyValues(String propertyName) {
        String[] values = config.getStringArray(propertyName);
        if(ArrayUtils.isEmpty(values)){
            throw new NullPointerException("Could not find the '" + propertyName + "' property.  Check if this has been properly configured");
        }
        return Arrays.stream(values).map(v -> {
            try {
                return AESUtil.isEncrypted(v) ? AESUtil.decrypt(AESUtil.removeEncryptionMarkers(v)) : v;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }).toArray(String[]::new);
    }

    /**
     * Returns all the property configurations stored within {@link #config}. If
     * {@link #config} is empty/does not currently hold any configurations, then
     * this method will return null.
     *
     * @return all the property configurations stored within {@link #config} as a
     * {@link Properties} object if present, otherwise null
     */
    public static @Nullable Properties getAllConfigsAsProperties(){
        Properties props = new Properties();
        Iterator<String> it = config.getKeys();
        while(it.hasNext()){
            String key = it.next();
            props.setProperty(key, config.getString(key));
        }
        return props.isEmpty() ? null : props;
    }

}