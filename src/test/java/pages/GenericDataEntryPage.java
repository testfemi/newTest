package pages;

import enums.CommonXpathLocators;
import interfaces.IDataEntry;
import utility.ElementUtil;

public abstract class GenericDataEntryPage extends BasePageObject implements IDataEntry {

    protected String
            containerXpath = String.format("//*[%s]", CommonXpathLocators.CASE_INSENSITIVE_TEXT_EQUALS),  //TODO - Refine these if needed
            labelXpath = String.format("//*[%s]", CommonXpathLocators.CASE_INSENSITIVE_TEXT_EQUALS); //TODO - Refine these if needed

    @Override
    public void enterData(String fieldLabel, String value) {
        ElementUtil.enterText(ElementUtil.getXpath(labelXpath, fieldLabel), value);
    }

    @Override
    public void enterData(String container, String fieldLabel, String value) {
        ElementUtil.enterText(
                ElementUtil.getXpath(containerXpath, container),
                ElementUtil.getXpath(labelXpath, fieldLabel), value);
    }

}