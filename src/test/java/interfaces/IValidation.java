package interfaces;

import java.util.List;

public interface IValidation {

    void validateTextFieldsAreVisible(List<String> listOfTextFieldNames);

    void validateResultsAreVisible(List<String> results);

    void validateTabsAreVisible(List<String> listOfTextFieldNames);

    boolean validateScreenIsVisible(String screenName);

    void validateButtonsAreDisabled(List<String> buttonNames, boolean areDisabled);

}