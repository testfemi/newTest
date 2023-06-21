package pages;

import enums.CommonXpathLocators;
import interfaces.ISelectable;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import utility.ElementUtil;

import java.util.List;

public class SASVIHomePage extends GenericDataEntryValidationPage implements ISelectable {

    private final String FIND_BTN_BY_TEXT_XPATH = String.format("//*[%s]" + CommonXpathLocators.CASE_SENSITIVE_TEXT_EQUALS), // TODO - Refine xpath
                                FIND_GRID_XPATH = String.format("//*[%s]" + CommonXpathLocators.CASE_SENSITIVE_TEXT_EQUALS), // TODO - Refine xpath
                              FIND_RESULT_XPATH = String.format("//*[%s]" + CommonXpathLocators.CASE_SENSITIVE_TEXT_EQUALS); // TODO - Refine xpath

    @Override
    public void selectButtonByText(String btnText) {
        ISelectable.super.selectButton(
                ElementUtil.waitForVisibilityOfElement(ElementUtil.getXpath(FIND_BTN_BY_TEXT_XPATH, btnText)));
    }

    @Override
    public void selectGrid(String gridName) {
        ElementUtil.waitForVisibilityOfElement(ElementUtil.getXpath(FIND_GRID_XPATH, gridName)).click();
    }

    @Override
    public void enterTextIntoSearchbar(String text) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void validateResultsAreVisible(@NotNull List<String> results) {
        results.forEach(result -> {
            Assert.assertTrue(ElementUtil.isElementVisible(ElementUtil.getXpath(FIND_RESULT_XPATH, result)));
        });
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