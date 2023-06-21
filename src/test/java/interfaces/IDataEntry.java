package interfaces;

import helper.ScenarioContext;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;

public interface IDataEntry {

    default void enterData(List<List<String>> data) {
        boolean threeColumnTable = validateDataTableStructure(data);
        data.forEach(row -> {
            if(threeColumnTable && StringUtils.isBlank(row.get(0))){
                enterData(row.get(1), row.get(2));
            }else if(threeColumnTable){
                enterData(row.get(0), row.get(1), row.get(2));
            }else{
                enterData(row.get(0), row.get(1));
            }
        });
    }

    void enterData(String fieldLabel, String value);

    void enterData(String container, String fieldLabel, String value);

    default HashMap<String, By> getLocatorMap() {
        return null;
    }

    default By getLocatorForField(String fieldLabel) {
        if(getLocatorMap() == null) {
            throw new NullPointerException("Locators for the " + ScenarioContext.currentPage + " page have not been defined");
        }
        return getLocatorMap().get(fieldLabel.toLowerCase());
    }

    private boolean validateDataTableStructure(List<List<String>> data){
        int size = data.get(0).size();
        if(size > 3 || size < 2){
            throw new InputMismatchException("The datatable needs to specify at least two or three columns to properly indicate identifier and input data values");
        }
        return size == 3; //True if size == 3, false if size == 2
    }

    void enterTextIntoSearchbar(String text);
}