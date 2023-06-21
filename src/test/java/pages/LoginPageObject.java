package pages;

import interfaces.INavigable;

public abstract class LoginPageObject extends GenericDataEntryPage implements INavigable {

    public abstract BasePageObject enterLoginCredentials(String username, String password);
    public abstract BasePageObject login();
    public abstract BasePageObject logout();

    public BasePageObject switchAccount(String username, String password){
        logout();
        enterLoginCredentials(username, password);
        return this;
    }
    public abstract boolean onLoginPage();
    public abstract boolean isLoggedIn(String username);

    public abstract boolean isLogoutDisplayed();

    public abstract void LogoutFromApplication();
}
