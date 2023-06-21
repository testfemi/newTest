package enums;

import helper.ScenarioContext;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import pages.*;
import pages.framework_training.TrainingFormPage;



import java.util.Objects;

/**
 * Enum for linking together the plain-text references for pages (which are used in the
 * ".feature" files) to the {@link Class} file for that page.  This is so that the framework
 * can automatically configure the page objects/instances for each of these pages when
 * required during execution.
 */
public enum Pages {

    /* SalesForce */
    Login_PAGE("Login Page", LoginPage.class),
    Generic_PAGES("Generic Page", GenericFunctionsPage.class),

    /* SauceDemo */

    SauceDemo1_PAGES("SauceDemo1 Page", SauceDemo1Page.class),


    /* SAS VI Pages */
    SASVI_HOME_PAGE("SASVI Home", SASVIHomePage.class),
    SASVI_ADVANCED_PAGE("SASVI Advanced", SASVIAdvancedPage.class),

    /* Framework Testing Pages (for Training & Development ONLY) */
    TRAINING_FORM_PAGE("TrainingForm", TrainingFormPage.class),;



   



    private final String pageRef; //The plain-english reference to use for referring to this object
    private String tabHandle; //The window/tab handle for the page if it is currently open in the browser
    private final Class<?> classForPage; //Class linked to this page (should not be null)
    private Object pageObject; //Instantiated object created from classForPage (can be/default is null)

    Pages(String pageRef, Class<?> classForPage){
        this.pageRef = pageRef;
        this.classForPage = classForPage;
    }

    Pages(String pageRef, Class<?> classForPage, Object pageObject){
        this.pageRef = pageRef;
        this.classForPage = classForPage;
        this.pageObject = pageObject;
    }

    /**
     * Set the {@link #pageObject} for this {@link Pages} enum.
     *
     * @param pageObject the instantiated page object
     */
    public void setPageObject(Object pageObject){
        this.pageObject = pageObject;
    }

    /**
     * Set the {@link #tabHandle} for this {@link Pages} enum.
     *
     * @param tabHandle the reference to the tab handle as a {@link String}
     */
    public void setTabHandle(String tabHandle) { this.tabHandle = tabHandle; }

    /**
     * Returns the {@link #pageRef} for this {@link Pages} enum.
     *
     * @return the {@link #pageRef} as a {@link String}
     */
    public String getPageRef(){
        return pageRef;
    }

    /**
     * Returns the {@link #pageObject} for this {@link Pages} enum.
     *
     * @return the {@link #pageObject} as an {@link Object}
     */
    public Object getPageObject(){
        return pageObject;
    }

    /**
     * Returns the {@link #classForPage} for this {@link Pages} enum.
     *
     * @return the {@link #classForPage} for this {@link Pages} enum
     */
    public Class<?> getClassForPage() { return classForPage; }

    /**
     * Returns the {@link #pageObject} applicable to the {@link Pages} enum
     * which has the {@link #pageRef} equal to the {@param pageRef} given.
     *
     * @param pageRef the {@link #pageRef} for the {@link Pages} enum to locate
     *                as a {@link String}
     * @return the {@link #pageObject} as an {@link Object}
     */
    public static Object getPageObjectFromPageRef(String pageRef){
        return Objects.requireNonNull(getPagesEnumFromPageRef(pageRef)).pageObject;
    }

    /**
     * Returns the {@link #classForPage} applicable to the {@link Pages} enum
     * which has the {@link #pageRef} equal to the {@param pageRef} given.
     *
     * @param pageRef the {@link #pageRef} for the {@link Pages} enum to locate
     *                as a {@link String}
     * @return the {@link #classForPage} for the applicable {@link Pages} enum
     */
    public static Class<?> getClassFromPageRef(String pageRef){
        return Objects.requireNonNull(getPagesEnumFromPageRef(pageRef)).classForPage;
    }

    /**
     * Returns the {@link Pages} enum which has the {@link #pageRef} equal to
     * the {@param pageRef} given.
     *
     * @param pageRef the {@link #pageRef} for the {@link Pages} enum to locate
     *                as a {@link String}
     * @return the {@link Pages} enum which has {@link #pageRef} equal to {@param pageRef}
     */
    public static Pages getPagesEnumFromPageRef(String pageRef){
        for (Pages page : Pages.values()) {
            if (pageRef.equalsIgnoreCase(page.pageRef)) {
                return page;
            }
        }
        return null;
    }

    /**
     * Attempts to cast the instantiated {@link #pageObject} to the given
     * {@param _class} type by implicitly calling {@link Class#cast(Object)}.
     *
     * @param _class the {@link Class} type to try and cast {@link #pageObject}
     *               to.
     * @return the {@link #pageObject} as the {param _class} type if casting is
     * successful
     * @throws RuntimeException if {@link #pageObject} cannot be cast to the
     * {@param _class} type given
     * @param <T> Generic type for illustrating the type of {@param _class} and
     *           bounding the return of this method to this type
     */
    public <T> T castPagesAs(Class<T> _class) {
        try {
            return _class.cast(pageObject);
        }catch (ClassCastException cannotCast){
            throw new RuntimeException("The current page '" + pageRef
                    + "' cannot use functions of the following page type: " + _class.getSimpleName());
        }
    }

    /**
     * Evaluates whether the {@link #pageObject} is an instance/type of
     * the {@param _class} given by implicitly calling
     * {@link Class#isInstance(Object)}.
     *
     * @param _class the {@link Class} to evaluate the type of
     * {@link #pageObject} against
     * @return true if {@link #pageObject} is an instance of {@param _class},
     * otherwise false
     */
    public boolean isPageOfType(Class<?> _class) {
        return _class.isInstance(pageObject);
    }

    /**
     * Returns the {@link #tabHandle} for this {@link Pages} enum.
     *
     * @return if {@link #tabHandle} is not null & {@link WebDriver#getWindowHandles()}
     * contains {@link #tabHandle}, return {@link #tabHandle} as a {@link String}, else
     * return null
     */
    public String getTabHandle(){
        return ScenarioContext.driver.getWindowHandles().contains(tabHandle)
                ? tabHandle
                : null;
    }

    /**
     * Instructs the {@link ScenarioContext#driver} to switch to the {@link #tabHandle}
     * by implicitly calling {@link WebDriver.TargetLocator#window(String)}.
     *
     * @see WebDriver.TargetLocator#window(String)
     * @throws RuntimeException if {@link #tabHandle} is null
     * @throws NoSuchWindowException cascaded from implicit call to
     * {@link WebDriver.TargetLocator#window(String)}
     */
    public void switchToTab(){
        if(tabHandle == null) {
            throw new RuntimeException(pageRef +" has no tab reference associated with it.");
        }
        ScenarioContext.driver.switchTo().window(tabHandle);
    }

    //TODO - Implement methods for opening/closing browser tabs for pages

    //public void killAllTabs()

    //public void openNewTab()

    //public void closeTab()

}