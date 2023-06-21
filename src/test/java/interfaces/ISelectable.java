package interfaces;

import helper.Log;
import helper.ScenarioContext;
import org.openqa.selenium.WebElement;

import java.util.Objects;

public interface ISelectable {

    default void selectButton(String btn){
        // Make calls to additional sub-routines if needed (for trying different ways of selecting a button)
        selectButtonByText(btn);
    }

    default void selectButtonByText(String btnText) {
        throw new RuntimeException(ScenarioContext.currentPage.getPageRef()
                + " does not implement ISelectable#selectButtonByText functionality");
    }

    default void selectButton(WebElement btnElem) {
        if(btnElem == null){
            Log.error("Cannot select button element/button element is null");
        }
        Objects.requireNonNull(btnElem).click();
    }

    default void selectTab(String tabName) {
        throw new RuntimeException(ScenarioContext.currentPage.getPageRef()
                + " does not implement ISelectable#selectTab functionality");
    }

    default void selectTab(WebElement tabElem) {
        if(tabElem == null){
            Log.error("Cannot select tab element/tab element is null");
        }
        Objects.requireNonNull(tabElem).click(); //TODO - Refine this on how exactly a tab is selected
    }

    default void selectCheckboxOption(String checkboxIdentifier, String option) {
        throw new RuntimeException(ScenarioContext.currentPage.getPageRef()
                + " does not implement ISelectable#selectCheckboxOption functionality");
    }

    default void selectGrid(String gridName) {
        throw new RuntimeException(ScenarioContext.currentPage.getPageRef()
                + " does not implement ISelectable#selectGrid functionality");
    }

    default void selectHighestProductAmount(String highestNum) {
        throw new RuntimeException(ScenarioContext.currentPage.getPageRef()
                + " does not implement ISelectable#selectHighestProductNumber functionality");
    }

    default void selectButton() {
        // Make calls to additional sub-routines if needed (for trying different ways of selecting a button)
    }
    }