package pages;

import helper.Log;
import helper.TestConfig;
import interfaces.IDataEntry;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utility.ElementUtil;

public class LoginPage extends LoginPageObject implements IDataEntry  {

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginBtn;

    @FindBy(id = "logout_sidebar_link") //TODO - Add this
    private WebElement logoutBtn;

    @FindBy(id = "") //TODO - Add this
    private WebElement userField;


    @FindBy(id = "react-burger-menu-btn") //TODO - Add this
    private WebElement burgerMenu;

    @FindBy(id = "logout_sidebar_link") //TODO - Add this
    private WebElement logoutLink;



    @Override
    public String getURL() {
        return TestConfig.getPropertyValue("test_login_url");
    }

    @Override
    public LoginPage enterLoginCredentials(String username, String password) {
        if(onLoginPage()){
            ElementUtil.enterText(usernameField, username);
            ElementUtil.enterText(passwordField, password);


        }else{
            throw new RuntimeException("Login page/fields not visible on the page");
        }
        return this;
    }

    @Override
    public LoginPage login() {
           loginBtn.click();
           return this;
    }

    @Override
    public LoginPage logout() {
        Log.error("Functionality for TestPageObject#logout not required for framework testing");
      //  logoutBtn.click();
        return this;
    }

    @Override
    public boolean onLoginPage() {
        return ElementUtil.isElementVisible(usernameField);
    }

    @Override
    public boolean isLoggedIn(String username) {
        if(username == null){
            return ElementUtil.isElementVisible(userField);
        }
        return userField.getText().contains(username);
    }

    @Override
    public boolean isLogoutDisplayed() {
        ElementUtil.click(burgerMenu);
        ElementUtil.waitForVisibilityOfElement(logoutLink);
        return ElementUtil.isElementVisible(logoutLink);

    }

    @Override
    public void LogoutFromApplication() {
        ElementUtil.waitForVisibilityOfElement(logoutLink);
        ElementUtil.click(logoutLink);
    }

    @Override
    public void enterTextIntoSearchbar(String text) {
        Log.error("Functionality for TestPageObject#enterTextIntoSearchbar not required for framework testing");
        throw new RuntimeException();
    }

}