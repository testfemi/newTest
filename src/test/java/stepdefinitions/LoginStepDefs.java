package stepdefinitions;

import helper.ScenarioContext;
import helper.TestConfig;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.LoginPageObject;

public class LoginStepDefs extends PageTracker {

    @When("the user enters the {string} user login credentials")
    public void the_user_enters_the_user_login_credentials(String userCredRef){
        ScenarioContext.currentPage.castPagesAs(LoginPageObject.class).enterLoginCredentials(
                TestConfig.getPropertyValue(userCredRef + "_username"),
                TestConfig.getPropertyValue(userCredRef + "_password"));
    }
    @And("I click the Login button")
    public void iClickTheLoginButton() {
        ScenarioContext.currentPage.castPagesAs(LoginPageObject.class).login();
    }

    @Then("the {string} user is logged in successfully")
    public void the_user_is_logged_in_successfully(String username){
        ScenarioContext.currentPage.castPagesAs(LoginPageObject.class).login();
        Assert.assertTrue(ScenarioContext.currentPage.castPagesAs(LoginPageObject.class).isLoggedIn(username));
    }

    @Then("the user login is unsuccessful")
    public void the_user_login_is_unsuccessful(){
        ScenarioContext.currentPage.castPagesAs(LoginPageObject.class).login();
        Assert.assertFalse(ScenarioContext.currentPage.castPagesAs(LoginPageObject.class).isLoggedIn(null));
    }


}
