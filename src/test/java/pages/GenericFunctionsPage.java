
package pages;

import helper.Log;
import interfaces.ISelectable;
import interfaces.IValidation;
import io.cucumber.datatable.DataTable;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import utility.ElementUtil;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GenericFunctionsPage extends LoginPageObject implements ISelectable, IValidation {

  //  @FindBy(xpath = "//img[@class='icon noicon']")
  //  public WebElement viewProfile;

    @FindBy(id = "add-to-cart-sauce-labs-bike-light")
    public WebElement viewProfile;


    @FindBy(xpath = ".//*[@class='profile-link-label']")
    private WebElement userProfile;
    @FindBy(xpath = "(.//*[@part='input'])[6] | (//*[@class=\" input\"])[1]")
    private WebElement editTitle;
    @FindBy(xpath = "(.//*[@class=' input'])[2]")
    private WebElement editCompanyName;
    @FindBy(xpath = "(.//*[@part='input'])[9] | (.//*[@class=' input'])[4]")
    private WebElement editMobile;
    @FindBy(xpath = "(.//*[@part='input'])[8] | (.//*[@class=' input'])[3]")
    private WebElement editPhone;
    @FindBy(xpath = ".//*[@class=' default input uiInput uiInputTextForAutocomplete uiInput--default uiInput--input uiInput uiAutocomplete uiInput--default uiInput--lookup']")
    private WebElement editManager;
    @FindBy(xpath = "(.//*[@class='slds-input'])[2]")
    private WebElement opportunitiesName;
    @FindBy(xpath = "(.//*[@class='slds-combobox__input slds-input'])[1]")
    private WebElement reportsTo;

    @FindBy(xpath = ".//*[contains(@id,'combobox-input')]")
    private WebElement accountName;

    @FindBy(xpath = ".//*[@name='SaveEdit']")
    private WebElement SaveOpportunity;
    private final String FIND_CHECKBOX_XPATH = "(//*[text()='%s'])[1]/../..//label[text()='%s']/parent::*/input",
            FIND_TEXT_FIELD_XPATH = "//*[text()='%1$s' or @placeholder='%1$s']",
            FIND_DYNAMIC_XPATH = "(.//*[contains(text(),'%1$s')])[2]",
            FIND_PLACEHOLDER_XPATH = ".//*[@placeholder='%1$s' or @name='%1$s']",
            FIND_Text_XPATH = ".//b[text()='1$s']",
            FIND_SAVE_XPATH = "(//*[text()='%1$s'])[2]";

    @FindBy(xpath = "//*[@id=\"0052w00000FwQB1_RelatedPermsetAssignmentList_body\"]")
    private WebElement permissionTabValidation;

    public void viewProfileClick() throws InterruptedException {
        Thread.sleep(3000);
        ElementUtil.jsClick(viewProfile);
    }

    public void closeDateValidation() throws InterruptedException {
        Thread.sleep(2000);
        ElementUtil.jsClick(ElementUtil.getXpath(".//*[@name='CloseDate']"));
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String date1= dateFormat.format(date);
        System.out.println("***************"+date1+"**************");
        Thread.sleep(2000);
        ElementUtil.enterText(ElementUtil.getXpath(".//*[@name='CloseDate']"),date1);
    }

    public void opportunityLinkSelection() {
        ElementUtil.jsClick(ElementUtil.getXpath("//*[@id=\"brandBand_2\"]/div/div/div[4]/div/div/div[2]/div/div[1]/div[2]/div[2]/div[1]/div/div/table/tbody/tr/th/span/a"));
    }

    public void userProfileClick() {
        Log.info("User Profile is : " +ElementUtil.getText(userProfile));
        ElementUtil.waitForVisibilityOfElement(userProfile);
        ElementUtil.jsClick(userProfile);
    }
    public boolean permissionSetLabel(String labelset){
        System.out.println(ElementUtil.getText(permissionTabValidation));
        return ElementUtil.getText(permissionTabValidation).trim().contains(labelset);
    }

    public void permissionCheck(DataTable tabledata) throws InterruptedException {
        Thread.sleep(8000);
        ElementUtil.scrollToElement(permissionTabValidation);
        List<String> strvalues = tabledata.asList();
        for (String strvalue : strvalues) {
            if (permissionSetLabel(strvalue)) {
                Log.info("=========Data is found==========");
            } else {
                Log.error("Data is not match " + permissionSetLabel(strvalue) + "===========" + strvalue);
            }
        }
    }

    public void appManager(String searchValue) throws InterruptedException {
        Thread.sleep(2000);
        ElementUtil.jsClick(ElementUtil.getXpath(".//*[@class='slds-icon-waffle']"));
        ElementUtil.enterText(ElementUtil.getXpath(".//*[@placeholder='Search apps and items...']"), searchValue);
        Thread.sleep(3000);
        if(searchValue.equals("Accounts")) {
            ElementUtil.jsClick(ElementUtil.getXpath(".//b[text()='Accounts']"));
        }else if(searchValue.equals("Sales")){
            ElementUtil.jsClick(ElementUtil.getXpath(".//b[text()='Sales']"));
        }
        else{
            ElementUtil.jsClick(ElementUtil.getXpath(".//b[text()='Opportunities']"));
        }
    }
    public void editReportsToName1(String reportsToNameValue) throws InterruptedException {
        Thread.sleep(3000);
        ElementUtil.jsClick(reportsTo);
        ElementUtil.enterText(reportsTo, reportsToNameValue);
        Thread.sleep(3000);
        ElementUtil.jsClick(ElementUtil.getXpath("(.//*[@class='slds-media slds-listbox__option slds-media_center slds-listbox__option_entity'])[2]"));
        Thread.sleep(3000);
    }

    public void accountNameCheck(String accountNameValue) throws InterruptedException {
        Thread.sleep(3000);
        ElementUtil.jsClick(accountName);
        ElementUtil.enterText(accountName,accountNameValue);
        Thread.sleep(3000);
        ElementUtil.jsClick(ElementUtil.getXpath("(.//*[@class='slds-media slds-listbox__option slds-media_center slds-listbox__option_entity'])[2]"));
        Thread.sleep(3000);
    }
    public void dropDownCheckOpportunity(int j,String dropDownValue) throws InterruptedException {
        Thread.sleep(3000);
        ElementUtil.jsClick(ElementUtil.getXpath("(.//*[@class='slds-combobox__input slds-input_faux slds-combobox__input-value'])["+j+"]"));
        ElementUtil.jsClick(ElementUtil.getXpath(FIND_TEXT_FIELD_XPATH, dropDownValue));
    }

    public void validationClick(String validateData) {
        //add if condition here
        ElementUtil.jsClick(ElementUtil.getXpath(FIND_TEXT_FIELD_XPATH, validateData));
    }
    public void validationSaveClick(String validateData) {
        //add if condition here
        ElementUtil.jsClick(ElementUtil.getXpath(FIND_SAVE_XPATH, validateData));
    }

    public void validationOpportunityNew(String validateData) {
        //add if condition here
        ElementUtil.jsClick(ElementUtil.getXpath("(.//*[text()='New'])[4]", validateData));
    }

    public void validationOpportunityClick(String validateData) {
        //add if condition here
        ElementUtil.jsClick(ElementUtil.getXpath(("(.//*[text()='Opportunities'])[2]"), validateData));
    }


    public void mainDropdownClick(int i) throws InterruptedException {
        ElementUtil.jsClick(ElementUtil.getXpath("(.//*[@class='slds-icon-utility-chevrondown slds-icon_container'])["+i+"]"));
    }

    public void ContactSelectOption() throws InterruptedException {
        ElementUtil.jsClick(ElementUtil.getXpath("//*[@id=\"brandBand_1\"]/div/div/div/div/div[2]/div/div[1]/div[2]/div[2]/div[1]/div/div/table/tbody/tr[1]/th/span/a"));
    }


    public void enterData(String fieldLabel, String value) {
        ElementUtil.enterText(ElementUtil.getXpath(FIND_PLACEHOLDER_XPATH, fieldLabel), value);
    }

    public void logoutClick(String logoutCheck) {
        ElementUtil.jsClick(ElementUtil.getXpath(FIND_DYNAMIC_XPATH, logoutCheck));

    }

    public void editTitle(String titleValue) throws InterruptedException {
        Thread.sleep(3000);
        ElementUtil.enterText(editTitle, titleValue);
    }

    public void editPencilIcon(int i) throws InterruptedException {
        ElementUtil.jsClick(ElementUtil.getXpath("(.//*[@class='inline-edit-trigger-icon slds-button__icon slds-button__icon_hint'])["+i+"]"));
    }
    public void editCompanyName(String companyNameValue) {
        ElementUtil.enterText(editCompanyName, companyNameValue);
    }


    public void opportunityName(String opportunityNameValue) {
        ElementUtil.enterText(opportunitiesName, opportunityNameValue);
    }

    public void saveOpportunityCreated() {
        ElementUtil.jsClick(SaveOpportunity);
    }
    public void editReportsToName(String reportsToNameValue) throws InterruptedException {
        Thread.sleep(3000);
        ElementUtil.jsClick(reportsTo);
        Thread.sleep(3000);
        ElementUtil.enterText(reportsTo, reportsToNameValue);
        Thread.sleep(3000);
        ElementUtil.jsClick(ElementUtil.getXpath("(.//*[text()='Save'])[3]"));
    }


    public void editPhone(String phoneValue) {
         ElementUtil.enterText(editPhone, phoneValue);
    }

    public void editMobile(String mobileValue) {
        ElementUtil.enterText(editMobile, mobileValue);
    }

    public void editManager(String managerValue) {
        ElementUtil.enterText(editManager, managerValue);
    }

    @Override
    public HashMap<String, By> getLocatorMap() {
        return super.getLocatorMap();
    }

    @Override
    public void enterTextIntoSearchbar(String text) {

    }

    @Override
    public String getURL() {
        return null;
    }

    @Override
    public BasePageObject enterLoginCredentials(String username, String password) {
        return null;
    }

    @Override
    public BasePageObject login() {
        return null;
    }

    @Override
    public BasePageObject logout() {
        return null;
    }

    @Override
    public boolean onLoginPage() {
        return false;
    }

    @Override
    public boolean isLoggedIn(String username) {
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
    public void validateTextFieldsAreVisible(List<String> listOfTextFieldNames) {

    }

    @Override
    public void validateResultsAreVisible(List<String> results) {

    }

    @Override
    public void validateTabsAreVisible(List<String> listOfTextFieldNames) {

    }

    @Override
    public boolean validateScreenIsVisible(String screenName) {
        return false;
    }

    @Override
    public void validateButtonsAreDisabled(List<String> buttonNames, boolean areDisabled) {

    }
}


