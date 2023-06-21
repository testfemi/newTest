package enums;

/**
 * Enum values representing common expressions and predicates used when writing XPATH locators.
 */
public enum CommonXpathLocators {

    CASE_INSENSITIVE_TEXT_EQUALS("translate(normalize-space(text()),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz') = translate(\"%s\",'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"),
    CASE_SENSITIVE_TEXT_EQUALS("normalize-space(text()) = \"%s\"]"),
    CASE_INSENSITIVE_ATTRIBUTE_EQUALS("translate(normalize-space(@%s),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz') = translate(\"%s\",'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"),
    CASE_SENSITIVE_ATTRIBUTE_EQUALS("normalize-space(@%s) = \"%s\"]"),
    CASE_INSENSITIVE_ATTRIBUTE_CONTAINS("contains(translate(normalize-space(@%s),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), translate(\"%s\",'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'))"),
    CASE_SENSITIVE_ATTRIBUTE_CONTAINS("contains(normalize-space(@%s), \"%s\")");

    private final String xpathLocator;

    CommonXpathLocators(String xpathLocator){
        this.xpathLocator = xpathLocator;
    }

    /**
     * Implicitly calls {{@link String#format(String, Object...)}} for formatting
     * this {@link #xpathLocator} with the {@param args} given.
     * 
     * @see String#format(String, Object...) 
     * @param args arguments referenced by the format specifiers in {@link #xpathLocator}
     * @return the formatted {@link #xpathLocator} as a {@link String}
     */
    public String format(Object... args){
        return String.format(xpathLocator, args);
    }

    /**
     * Overrides the inherited {@link Object#toString()} method to return
     * the {@link #xpathLocator} for the current {@link CommonXpathLocators}
     * enum.
     *
     * @return the {@link #xpathLocator} as a {@link String}
     */
    @Override
    public String toString() {
        return xpathLocator;
    }

}