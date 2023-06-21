package pages.framework_training;

import helper.Log;
import helper.TestConfig;
import interfaces.IValidation;
import interfaces.ISelectable;
import org.jetbrains.annotations.NotNull;
import pages.BasePageObject;
import pages.LoginPageObject;

import org.testng.Assert;
import utility.ElementUtil;

import java.util.List;

public class TrainingFormPage extends LoginPageObject implements ISelectable, IValidation {

    private final String FIND_CHECKBOX_XPATH = "//*[text()='%s']/../..//label[text()='%s']/parent::*/input",
                       FIND_TEXT_FIELD_XPATH = "//*[text()='%1$s' or @placeholder='%1$s']";

    @Override
    public String getURL() {
       return TestConfig.getPropertyValue("test_login_url");

    }

    @Override
    public BasePageObject enterLoginCredentials(String username, String password) {
        Log.info("Functionality for TestPageObject#enterLoginCredentials not required for framework testing");
        return this;
    }

    @Override
    public BasePageObject login() {
        Log.info("Functionality for TestPageObject#login not required for framework testing");
        return this;
    }

    @Override
    public BasePageObject logout() {
        Log.info("Functionality for TestPageObject#logout not required for framework testing");
        return this;
    }

    @Override
    public boolean onLoginPage() {
        Log.info("Functionality for TestPageObject#onLoginPage not required for framework testing");
        return true;
    }

    @Override
    public boolean isLoggedIn(String username) {
        Log.info("Functionality for TestPageObject#isLoggedIn not required for framework testing");
        return false;
    }

    @Override
    public boolean isLogoutDisplayed() {
        return false;
    }

    @Override
    public void LogoutFromApplication() {

    }

    @Override
    public void selectCheckboxOption(String checkboxIdentifier, String option) {
        ElementUtil.jsClick(ElementUtil.getXpath(FIND_CHECKBOX_XPATH, checkboxIdentifier, option));

    }

    @Override
    public void validateTextFieldsAreVisible(@NotNull List<String> listOfTextFieldNames) {
        for(String textFieldName : listOfTextFieldNames){
            Assert.assertTrue(ElementUtil.isElementVisible(ElementUtil.getXpath(FIND_TEXT_FIELD_XPATH, textFieldName)));
        }
    }

    @Override
    public void validateResultsAreVisible(List<String> results) {
        throw new RuntimeException("Not Implemented Yet");
    }

    @Override
    public void validateTabsAreVisible(List<String> listOfTextFieldNames) {
        throw new RuntimeException("Not Implemented Yet");
    }

    @Override
    public boolean validateScreenIsVisible(String screenName) {
        throw new RuntimeException("Not Implemented Yet");
    }

    @Override
    public void validateButtonsAreDisabled(List<String> buttonNames, boolean areDisabled) {
        throw new RuntimeException("Not Implemented Yet");
    }

    @Override
    public void enterTextIntoSearchbar(String text) {
        throw new RuntimeException("Not Implemented Yet");
    }
}