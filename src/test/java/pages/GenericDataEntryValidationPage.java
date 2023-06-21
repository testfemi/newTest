package pages;

import enums.CommonXpathLocators;
import interfaces.IValidation;
import utility.ElementUtil;

import java.util.List;

public abstract class GenericDataEntryValidationPage extends GenericDataEntryPage implements IValidation {

    //TODO - Refine these if needed
    protected String txtFieldXpath = String.format("//input[(%s) and (%s)]"
            + CommonXpathLocators.CASE_SENSITIVE_ATTRIBUTE_EQUALS.format("type", "text") //For text fields/elements only
            + CommonXpathLocators.CASE_SENSITIVE_TEXT_EQUALS
    );

    @Override
    public void validateTextFieldsAreVisible(List<String> listOfTextFieldNames){
        for(String txtFieldName : listOfTextFieldNames){
            ElementUtil.waitForVisibilityOfElement(ElementUtil.getXpath(txtFieldXpath, txtFieldName));
        }
    }

    public abstract String getURL();
}