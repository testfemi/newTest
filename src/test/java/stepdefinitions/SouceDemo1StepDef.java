package stepdefinitions;

import enums.Pages;
import helper.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.LoginPageObject;
import pages.SauceDemo1Page;

public class SouceDemo1StepDef extends PageTracker {

    @Then("the {string} user is logged in")
    public void the_user_is_logged_in(String username) {
        Assert.assertTrue(ScenarioContext.currentPage.castPagesAs(LoginPageObject.class).isLogoutDisplayed());
    }

    @When("the user selects the Highest price from the list of product and add to cart")
    public void the_user_selects_the_highest_price_from_the_list_of_product_and_add_to_cart() {
        Pages page = Pages.getPagesEnumFromPageRef("SauceDemo1 Page");
        setPage(page);
        ScenarioContext.currentPage.castPagesAs(SauceDemo1Page.class).selectProductHighestPrice();

    }
    @When("the user selects the Lowest price from the list of product and add to cart")
    public void the_user_selects_the_lowest_price_from_the_list_of_product_and_add_to_cart() {
        Pages page = Pages.getPagesEnumFromPageRef("SauceDemo1 Page");
        setPage(page);
        ScenarioContext.currentPage.castPagesAs(SauceDemo1Page.class).selectProductLowestPrice();
    }
    @When("I click on the Sort dropdown and select {string}")
    public void i_click_on_the_sort_dropdown_and_select(String dropdownOption) throws InterruptedException {
        Pages page = Pages.getPagesEnumFromPageRef("SauceDemo1 Page");
        setPage(page);
        ScenarioContext.currentPage.castPagesAs(SauceDemo1Page.class).selectFilter(dropdownOption);
    }

    @When("I click on the logout button")
    public void i_click_on_the_logout_button() {
        Pages page = Pages.getPagesEnumFromPageRef("Login Page");
        setPage(page);
        ScenarioContext.currentPage.castPagesAs(LoginPageObject.class).LogoutFromApplication();
    }


}

