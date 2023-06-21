package stepdefinitions;

import enums.Pages;
import helper.Log;
import helper.ScenarioContext;
import interfaces.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.jetbrains.annotations.NotNull;

import org.openqa.selenium.WindowType;

import org.testng.Assert;
import pages.GenericFunctionsPage;


import java.util.List;
import java.util.Objects;

public class CommonStepDefs extends PageTracker {

    /**
     * Ensures that a browser tab is configured for the given {@param pageRef} and
     * navigates to it by implicitly calling {@link INavigable#navigateTo()}.  This
     * method also implicitly calls {@link PageTracker#setPage(Pages)} as part of its
     * implementation.
     *
     * @see INavigable#navigateTo()
     * @see PageTracker#setPage(Pages)
     * @param pageRef
     *          - The plain-english reference used to refer to the
     *          page you wish to navigate to.  This should correspond
     *          with one of the enum's {@link Pages#pageRef} value in
     *          {@link Pages}.
     */
    @Given("the user navigates to the {string} page")
    public void the_user_navigates_to_the_page(String pageRef) {
        //Set page via PageTracker#setPage
        Pages page = Pages.getPagesEnumFromPageRef(pageRef);
        setPage(page);

        //Get current tab for page (if exists)
        String handle = page.getTabHandle();

        /* If page already has a browser tab, switch
        to this tab rather than creating a new one */
        if(handle != null){

            /* If this browser tab IS NOT currently selected, switch
            to it, otherwise just continue (as we are already on the correct
            browser tab) */
            Log.info("Browser tab already exists for the %s page", page.getPageRef());
            if(!ScenarioContext.driver.getWindowHandle().equals(handle)){
                Log.info("Switching to the existing browser tab (%s) for the %s page",
                        handle,
                        page.getPageRef());
                ScenarioContext.driver.switchTo().window(handle);
            }

            //Otherwise, the browser tab for the page MUST already be selected/open
            Log.info("Existing browser tab (%s) for the %s page is currently selected",
                    handle,
                    page.getPageRef());

            //Log warning that we are re-navigating to the page
            Log.info("Re-navigating to the %s page", page.getPageRef());
        }

        /* Else, we need to assign a browser tab for this page:

            if the current URL of the current browser tab equals "data;"
            (i.e., the default tab which opens when launching the WebDriver), then
            use this tab for the page
        */
        else if(ScenarioContext.driver.getCurrentUrl().equals("data:,")){
            Log.info("No browser tab currently allocated for the %s page", page.getPageRef());
            Log.info("Allocating the default (i.e. data:,) browser tab for the %s page", page.getPageRef());
            page.setTabHandle(ScenarioContext.driver.getWindowHandle());
            Log.info("Browser tab (%s) allocated to the %s page",
                    page.getTabHandle(),
                    page.getPageRef());
        }

        /* Else, If no browser tab is available/currently allocated to this page, create
        and allocate a new tab to this page and switch to it */
        else {
            Log.info("No browser tab currently allocated for the %s page", page.getPageRef());
            //Create & switch to a new browser tab
            Log.info("Creating and switching to new browser tab");
            ScenarioContext.driver.switchTo().newWindow(WindowType.TAB);
            //Allocate this new tab to the page
            page.setTabHandle(ScenarioContext.driver.getWindowHandle());
            Log.info("Browser tab (%s) allocated to the %s page",
                    page.getTabHandle(),
                    page.getPageRef());
        }

        /* Cast page to our desired interface (i.e. INavigableURL)
        and navigate to the page via URL */
        ScenarioContext.currentPage.castPagesAs(INavigable.class).navigateTo();
    }

    /**
     * Configures the current browser tab selected for the given {@param pageRef} and
     * overrides the configuration for the previous page allocated to this browser tab.
     * This method also implicitly calls {@link PageTracker#setPage(Pages)} as part of
     * its implementation.
     * <p>
     * (NOTE: this implementation DOES NOT perform the navigation operation to the page,
     * see {@link #the_user_navigates_to_the_page} if you require this behaviour
     * as well).
     *
     * @see #the_user_navigates_to_the_page
     * @see PageTracker#setPage(Pages)
     * @param pageRef the plain-english reference used to refer to the
     *          page you wish to navigate to.  This should correspond
     *          with one of the enum's {@link Pages#pageRef} value in
     *          {@link Pages}.
     */
    @Given("the user is on the {string} page")
    public void the_user_is_on_the_page(String pageRef) {
        //Set page via PageTracker#setPage
        Pages page = Pages.getPagesEnumFromPageRef(pageRef);
        setPage(page);

        //Get current tab for page (if exists)
        String handle = page.getTabHandle();

        /* If page already has a browser tab, switch
        to this tab rather than creating a new one */
        if(handle != null){

            /* If this browser tab IS NOT currently selected, switch
            to it, otherwise just continue (as we are already on the correct
            browser tab) */
            Log.info("Browser tab already exists for the %s page", page.getPageRef());
            if(!ScenarioContext.driver.getWindowHandle().equals(handle)){
                Log.info("Switching to the existing browser tab (%s) for the %s page",
                        handle,
                        page.getPageRef());
                ScenarioContext.driver.switchTo().window(handle);
            }

            //Otherwise, the browser tab for the page MUST already be selected/open
            Log.info("Existing browser tab (%s) for the %s page is currently selected",
                    handle,
                    page.getPageRef());
        }

        /* Else, we need to assign a browser tab for this page:

            if the current URL of the current browser tab equals "data;"
            (i.e., the default tab which opens when launching the WebDriver), then
            use this tab for the page
        */
        else if(ScenarioContext.driver.getCurrentUrl().equals("data:,")){
            Log.info("No browser tab currently allocated for the %s page", page.getPageRef());
            Log.info("Allocating the default (i.e. data:,) browser tab for the %s page", page.getPageRef());
            page.setTabHandle(ScenarioContext.driver.getWindowHandle());
            Log.info("Browser tab (%s) allocated to the %s page",
                    page.getTabHandle(),
                    page.getPageRef());
        }

        /* Else, If no browser tab is available/currently allocated to this page, create
        and allocate a new tab to this page and switch to it */
        else {
            Log.info("No browser tab currently allocated for the %s page", page.getPageRef());
            Log.info("Proceeding to override and re-allocate the current browser tab to the %s page instead",
                    page.getPageRef());

            //Allocate the current browser tab to this page instead
            Log.info("Allocating the current browser tab to the %s page instead", page.getPageRef());
            page.setTabHandle(ScenarioContext.driver.getWindowHandle());
            Log.info("Current Browser tab (%s) allocated to the %s page",
                    page.getTabHandle(),
                    page.getPageRef());

            //Deallocate the browser tab handle from the previous page (i.e. override and set to null)
            Log.info("De-allocating browser tab for the %s page", ScenarioContext.previousPage.getPageRef());
            assert Objects.equals(ScenarioContext.previousPage.getTabHandle(), ScenarioContext.driver.getWindowHandle())
                    : "Browser tab handles do not match, cannot de-allocate existing browser tab configuration for the "
                    + ScenarioContext.previousPage.getPageRef() + " page";
            ScenarioContext.previousPage.setTabHandle(null);
            Log.info("Current Browser tab (%s) de-allocated from the %s page",
                    ScenarioContext.driver.getWindowHandle(),
                    ScenarioContext.previousPage.getPageRef());
        }
    }

    @When("the user enters the following onto the page:")
    public void the_user_enters_the_following_onto_the_current_page(List<List<String>> data) {
        the_user_enters_the_following_onto_the_page(null, data);
    }

    @When("the user enters the following onto the {string} page:")
    public void the_user_enters_the_following_onto_the_page(String pageRef, List<List<String>> data) {
        if(pageRef != null){
            setPage(pageRef);
        }
        Log.info("Inputting data into %d elements on the %s page", data.size(), pageRef);
        if(ScenarioContext.currentPage.isPageOfType(IDataEntry.class)){
            ScenarioContext.currentPage
                    .castPagesAs(IDataEntry.class)
                    .enterData(data);
        }else{
            RuntimeException err = new RuntimeException("The " + pageRef + " page does not implement an IDataEntry interface");
            Log.error("RUNTIME ERROR:" , err);
        }
    }

    @Given("the user enters {string} into the searchbar")
    public void the_user_enters_into_the_searchbar(String text) {
        Log.info("Enter %s into the searchbar on the %s page",
                text,
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(IDataEntry.class)){
            ScenarioContext.currentPage
                    .castPagesAs(IDataEntry.class)
                    .enterTextIntoSearchbar(text);
        }else{
            throw new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an IDataEntry interface");
        }
    }

    @Given("the user selects the {string} tab")
    public void the_user_selects_the_tab(String tabName) {
        Log.info("Selecting the %s tab on the %s page",
                tabName,
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(ISelectable.class)){
            ScenarioContext.currentPage
                    .castPagesAs(ISelectable.class)
                    .selectTab(tabName);
        }else{
            RuntimeException err = new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an ISelectable interface");
            Log.error("RUNTIME ERROR:" , err);
        }
    }

    @Given("the user selects the {string} button")
    public void the_user_selects_the_button(String btn) {
        Log.info("Selecting the %s button on the %s page",
                btn,
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(ISelectable.class)){
            ScenarioContext.currentPage
                    .castPagesAs(ISelectable.class)
                    .selectButton(btn);
        }else{
            RuntimeException err = new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an ISelectable interface");
            Log.error("RUNTIME ERROR:" , err);
        }
    }

    @And("the user selects the {string} grid")
    public void the_user_selects_the_grid(String gridName) {
        Log.info("Selecting the %s grid on the %s page",
                gridName,
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(ISelectable.class)){
            ScenarioContext.currentPage
                    .castPagesAs(ISelectable.class)
                    .selectGrid(gridName);
        }else{
            RuntimeException err = new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an ISelectable interface");
            Log.error("RUNTIME ERROR:" , err);
        }
    }

    @Given("the user selects the {string} option from the {string} checkbox")
    public void the_user_selects_the_option_from_checkbox(String option, String checkboxIdentifier) {
        Log.info("Selecting the %s option from the %s checkbox on the %s page",
                option,
                checkboxIdentifier,
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(ISelectable.class)){
            ScenarioContext.currentPage
                    .castPagesAs(ISelectable.class)
                    .selectCheckboxOption(checkboxIdentifier, option);
        }else{
            RuntimeException err = new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an ISelectable interface");
            Log.error("RUNTIME ERROR:" , err);
        }
    }

    @And("the user selects the rows with the following data:")
    public void the_user_selects_the_rows_with_the_following_data(List<List<String>> data) {
        the_user_selects_the_rows_in_the_table_with_the_following_data(null, data);
    }

    @And("the user selects the rows in the {string} table with the following data:")
    public void the_user_selects_the_rows_in_the_table_with_the_following_data(String tableName, List<List<String>> data) {
        Log.info("Selecting %s in the %stable on the %s page which has the following data:\r\n\t%s",
                data.size() == 1 ? "1 row": data.size() + " rows",
                tableName == null ? "" : tableName + " ",
                ScenarioContext.currentPage.getPageRef(),
                data);
        if(ScenarioContext.currentPage.isPageOfType(ITable.class)){
            ScenarioContext.currentPage
                    .castPagesAs(ITable.class)
                    .selectRows(tableName, data);
        }else{
            throw new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an ITable interface");
        }
    }

    @Then("the user validates that the rows with the following data have been selected:")
    public void the_user_validates_that_the_rows_with_the_following_data_have_been_selected(@NotNull List<List<String>> data) {
        the_user_validates_that_the_rows_in_the_table_with_the_following_data_have_been_selected(null, data);
    }

    @Then("the user validates that the rows in the {string} table with the following data have been selected:")
    public void the_user_validates_that_the_rows_in_the_table_with_the_following_data_have_been_selected(String tableName, @NotNull List<List<String>> data) {
        Log.info("Validating that %s in the %stable on the %s page have been selected",
                data.size() == 1 ? "1 row": data.size() + " rows",
                tableName == null ? "" : tableName + " ",
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(ITable.class)){
            ScenarioContext.currentPage
                    .castPagesAs(ITable.class)
                    .validateRowsAreSelected(tableName, data);
        }else{
            throw new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an ITable interface");
        }
    }

    @Then("the user validates that all rows have been selected")
    public void the_user_validates_that_all_rows_have_been_selected() {
        the_user_validates_that_all_rows_in_the_table_have_been_selected(null);
    }

    @Then("the user validates that all rows in the {string} table have been selected")
    public void the_user_validates_that_all_rows_in_the_table_have_been_selected(String tableName) {
        Log.info("Validating that all rows from the %stable on the %s page have been selected",
                tableName == null ? "" : tableName + " ",
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(ITable.class)){
            ScenarioContext.currentPage
                    .castPagesAs(ITable.class)
                    .validateAllRowsAreSelected(tableName);
        }else{
            throw new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an ITable interface");
        }
    }

    @Then("the user validates that the latest row with the following data is visible:")
    public void the_user_validates_that_the_latest_row_with_the_following_data_is_visible(List<List<String>> data) {
        the_user_validates_that_the_latest_row_with_the_following_data_is_visible(null, data);
    }

    @Then("the user validates that the latest row in the {string} table with the following data is visible:")
    public void the_user_validates_that_the_latest_row_with_the_following_data_is_visible(String tableName, @NotNull List<List<String>> data) {
        Log.info("Validating that the latest row in the %stable on the %s page is visible",
                tableName == null ? "" : tableName + " ",
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(ITable.class)){
            ScenarioContext.currentPage
                    .castPagesAs(ITable.class)
                    .validateVisibilityOfRows(tableName, data, true);
        }else{
            throw new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an ITable interface");
        }
    }

    @Then("the user validates that the rows with the following data are visible:")
    public void the_user_validates_that_the_rows_with_the_following_data_are_visible(List<List<String>> data) {
        the_user_validates_that_the_rows_in_the_table_with_the_following_data_are_visible(null, data);
    }

    @Then("the user validates that the rows in the {string} table with the following data are visible:")
    public void the_user_validates_that_the_rows_in_the_table_with_the_following_data_are_visible(String tableName, @NotNull List<List<String>> data) {
        Log.info("Validating that %s in the %stable on the %s page are visible",
                data.size() == 1 ? "1 row": data.size() + " rows",
                tableName == null ? "" : tableName + " ",
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(ITable.class)){
            ScenarioContext.currentPage
                    .castPagesAs(ITable.class)
                    .validateVisibilityOfRows(tableName, data, true);
        }else{
            throw new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an ITable interface");
        }
    }

    @Then("the user validates that the rows with the following data are not visible:")
    public void the_user_validates_that_the_rows_with_the_following_data_are_not_visible(List<List<String>> data) {
        the_user_validates_that_the_rows_in_the_table_with_the_following_data_are_visible(null, data);
    }

    @Then("the user validates that the rows in the {string} table with the following data are not visible:")
    public void the_user_validates_that_the_rows_in_the_table_with_the_following_data_are_not_visible(String tableName, @NotNull List<List<String>> data) {
        Log.info("Validating that %s in the %stable on the %s page are not visible",
                data.size() == 1 ? "1 row": data.size() + " rows",
                tableName == null ? "" : tableName + " ",
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(ITable.class)){
            ScenarioContext.currentPage
                    .castPagesAs(ITable.class)
                    .validateVisibilityOfRows(data, false);
        }else{
            throw new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an ITable interface");
        }
    }

    @Then("the following text fields are visible:")
    public void the_following_text_fields_are_visible(@NotNull List<String> data) {
        Log.info("Validating that %d text fields/elements are visible on the %s page",
                data.size(),
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(IValidation.class)){
            ScenarioContext.currentPage
                    .castPagesAs(IValidation.class)
                    .validateTextFieldsAreVisible(data);
        }else{
            RuntimeException err = new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an IValidation interface");
            Log.error("RUNTIME ERROR:" , err);
        }
    }

    @Then("the following tabs are visible:")
    public void the_following_tabs_are_visible(@NotNull List<String> data) {
        Log.info("Validating that %d tabs are visible on the %s page",
                data.size(),
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(IValidation.class)){
            ScenarioContext.currentPage
                    .castPagesAs(IValidation.class)
                    .validateTabsAreVisible(data);
        }else{
            RuntimeException err = new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an IVisible interface");
            Log.error("RUNTIME ERROR:" , err);
        }
    }

    @Then("the page displays the following results:")
    public void the_following_results_are_displayed(@NotNull List<String> data) {
        Log.info("Validating that %d text fields/elements are visible on the %s page",
                data.size(),
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(IValidation.class)){
            ScenarioContext.currentPage
                    .castPagesAs(IValidation.class)
                    .validateResultsAreVisible(data);
        }else{
            throw new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an IValidation interface");
        }
    }

    @Then("the {string} screen is displayed")
    public void the_screen_is_displayed(String screenName) {
        Log.info("Validating that %s screen is displayed on the %s page",
                screenName,
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(IValidation.class)){
            boolean isDisplayed = ScenarioContext.currentPage
                    .castPagesAs(IValidation.class)
                    .validateScreenIsVisible(screenName);
            Assert.assertTrue(isDisplayed);
        }else{
            throw new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an IVisible interface");
        }
    }

    @Then("the following buttons are accessible:")
    public void the_following_buttons_are_accessible(@NotNull List<String> buttonNames) {
        Log.info("Validating that %d buttons are accessible on the %s page",
                buttonNames.size(),
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(IValidation.class)){
            ScenarioContext.currentPage
                    .castPagesAs(IValidation.class)
                    .validateButtonsAreDisabled(buttonNames, false); // TODO - Refine this method in interface
        }else{
            throw new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an IValidation interface");
        }

    }

    @Then("the following buttons are disabled:")
    public void the_following_buttons_are_disabled(@NotNull List<String> buttons) {
        Log.info("Validating that %d buttons are disabled on the %s page",
                buttons.size(),
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(IValidation.class)){
            ScenarioContext.currentPage
                    .castPagesAs(IValidation.class)
                    .validateButtonsAreDisabled(buttons, true); // TODO - Refine this method in interface
        }else{
            throw new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an IValidation interface");
        }

    }

    @Then("the user successfully downloads the {string} file")
    public void the_user_successfully_downloads_the_pdf_file(String fileType) {
        Log.info("Downloading the %s from the %s page",
                fileType,
                ScenarioContext.currentPage.getPageRef());
        if(ScenarioContext.currentPage.isPageOfType(IDownloadable.class)){
            IDownloadable page = ScenarioContext.currentPage.castPagesAs(IDownloadable.class);
            // Download the file first
            page.downloadFileType(fileType);
            // Validate that the file has been downloaded correctly
            Assert.assertTrue(page.isSuccessfullyDownloaded());
        }else{
            throw new RuntimeException("The " + ScenarioContext.currentPage.getPageRef() + " page does not implement an IDownloadable interface");
        }
    }

    @When("the user selects the {string} from the list of product")
    public void theUserSelectsTheFromTheListOfProduct(String arg0, int arg1) {
    }



}