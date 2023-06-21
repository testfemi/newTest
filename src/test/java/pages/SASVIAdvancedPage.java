package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import utility.ElementUtil;
import interfaces.IDataEntry;

import java.util.List;

public class SASVIAdvancedPage extends GenericDataEntryValidationPage implements IDataEntry {

    @FindBy(id = "")
    private WebElement searchBar;

    @Override
    public void enterTextIntoSearchbar(String text) {
        ElementUtil.enterText(searchBar, text);
        ElementUtil.waitForVisibilityOfElement(searchBar).sendKeys(text);
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
    public String getURL() {
        return null;
    }
}