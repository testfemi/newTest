package enums;

import org.openqa.selenium.remote.Browser;

/**
 * Enum for linking together the available browser options from {@link Browser} class.
 */
public enum BrowserOptions {
    CHROME(Browser.CHROME),
    EDGE(Browser.EDGE),
    IE(Browser.IE),
    FIREFOX(Browser.FIREFOX);

    private final Browser browser;

    BrowserOptions(Browser browser) {
        this.browser = browser;
    }

    /**
     * Get the human-readable name for the browser option by returning
     * the value from implicitly calling {@link Browser#browserName()}.
     * <p>
     * @see Browser#browserName()
     * @return the human-readable browser name as a {@link String}
     */
    public String browserName() {
        return browser.browserName();
    }

}
