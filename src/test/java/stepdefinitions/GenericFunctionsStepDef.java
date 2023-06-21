package stepdefinitions;

import enums.Pages;
import helper.Log;
import helper.ScenarioContext;
import interfaces.IValidation;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.BasePageObject;
import pages.GenericFunctionsPage;
import utility.ElementUtil;

import java.util.List;

public class GenericFunctionsStepDef extends PageTracker {

    @When("I click on the {string} from the top Right hand Menu Bar")
    public void i_click_on_the_from_the_top_right_hand_menu_bar(String string) throws InterruptedException {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        Thread.sleep(10000);
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).viewProfileClick();
        Log.info("Click on View profile Successful");

    }

    @And("I Click {string} from the dropdown profile list")
    public void i_click_from_the_dropdown_profile_list(String string) {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).userProfileClick();
        Log.info("Click on User Profile Successful");

    }

    @And("I click on the {string} from the middle right hand Menu")
    public void i_click_on_the_from_the_middle_right_hand_menu(String validateData) {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).validationClick(validateData);
        Log.info("Click on User Detail Successful");
    }

    @Then("the following permission sets are visible:")
    public void the_following_permission_sets_are_visible(DataTable datatable) throws InterruptedException {
        Thread.sleep(20000);
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        Log.info("Permission Sets are visible as per the given data i.e . " + datatable.entries());
        Thread.sleep(10000);
        ScenarioContext.currentPage.castPagesAs(BasePageObject.class).changeIframeByTitle("Salesforce - Professional Edition");
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).permissionCheck(datatable);
        ScenarioContext.currentPage.castPagesAs(BasePageObject.class).defaultIframe();

    }

    @And("I click {string} from the middle right hand Menu")
    public void i_click_from_the_middle_right_hand_menu(String validateData) {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).validationClick(validateData);
    }

    @And("I enter the following onto the below page:")
    public void i_enter_the_following_onto_the_below_page(DataTable dataTable) throws InterruptedException {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        Thread.sleep(3000);
        List<List<String>> data = dataTable.cells();
        for (int i = 0; i < dataTable.cells().size(); i++) {
            if (data.get(i).get(0).equals("Title")) {
                ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).editTitle(data.get(i).get(1));
            } else if (data.get(i).get(0).equals("Phone")) {
                ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).editPhone(data.get(i).get(1));
            } else if (data.get(i).get(0).equals("Mobile")) {
                ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).editMobile(data.get(i).get(1));
            } else if (data.get(i).get(0).equals("Manager")) {
                ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).editManager(data.get(i).get(1));
            } else if (data.get(i).get(0).equals("Company Name")) {
                ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).editCompanyName(data.get(i).get(1));
            } else if (data.get(i).get(0).equals("Reports To")) {
                ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).editReportsToName1(data.get(i).get(1));
            } else if (data.get(i).get(0).equals("Account Name")) {
                ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).accountNameCheck(data.get(i).get(1));
            } else if (data.get(i).get(0).equals("Opportunity Name")) {
                ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).opportunityName(data.get(i).get(1));
            } else {
                ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).enterData(data.get(i).get(0), data.get(i).get(1));
            }
        }
    }

    @And("I click {string} Button")
    public void i_click_button_to_save_the_records(String validateData) throws InterruptedException {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        Thread.sleep(3000);
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).validationClick(validateData);
    }

    @When("I click {string} Button from Contact")
    public void i_click_button_from_contact(String validateData) throws InterruptedException {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        Thread.sleep(3000);
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).validationSaveClick(validateData);
    }

    @And("I click {string} in the top right corner")
    public void i_click_in_the_top_right_corner(String validateData) {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).validationClick(validateData);
    }

    @And("I Click {string} from the dropdown profile list!")
    public void i_click_logout_from_the_dropdown_profile_list(String logoutCheck) {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).logoutClick(logoutCheck);
        Log.info("Clicked on Logout");

    }

    //-------------------------------------
    @When("I click on {string} from the top Menu Bar")
    public void i_click_on_from_the_top_menu_bar(String validateData) {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).validationClick(validateData);
    }

    @When("I click on {string} from the top Menu")
    public void i_click_on_from_the_top_menu(String validateData) {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).validationSaveClick(validateData);
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).validationClick(validateData);
    }

    @And("I click on {string} from the top Menu Bar and select {string} from the dropbox")
    public void i_click_on_from_the_top_menu_bar_and_select_from_the_dropbox(String validateData, String validateData2) throws InterruptedException {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).validationClick(validateData);
        if (validateData.equals("Contacts")) {
            ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).mainDropdownClick(3);
            ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).validationClick(validateData2);
        }
    }

    @And("I click on the Pencil Icon field next to {string} to edit")
    public void i_click_on_the_pencil_icon_field_next_to_to_edit(String string) throws InterruptedException {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        if (string.equals("Title")) {
            ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).editPencilIcon(3);
        } else if (string.equals("Department")) {
            ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).editPencilIcon(9);
        }
    }

    @And("I click on the {string}")
    public void i_click_on_the(String validateData) {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).validationClick(validateData);
    }

    @When("I select a contact from the list of Contacts")
    public void i_select_a_contact_from_the_list_of_contacts() throws InterruptedException {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        Thread.sleep(2000);
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).ContactSelectOption();
    }

    @When("I click {string} Link from the right hand side")
    public void i_click_link_from_the_right_hand_side(String validateData) {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).validationOpportunityClick(validateData);
    }

    @When("I click {string} Button for Opportunities")
    public void i_click_button_for_opportunities(String validateData) throws InterruptedException {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        Thread.sleep(3000);
        if (validateData.equals("New")) {
            ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).validationOpportunityNew(validateData);
        } else if (validateData.equals("Save")) {
            ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).saveOpportunityCreated();
            Thread.sleep(2000);
            ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).saveOpportunityCreated();

        }
    }

    @When("I select the following from the dropdwon lists")
    public void i_select_the_following_from_the_dropdwon_lists(DataTable dataTableValue) throws InterruptedException {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        Thread.sleep(3000);
        List<List<String>> data = dataTableValue.cells();
        for (int i = 0; i < dataTableValue.cells().size(); i++) {
            if (data.get(i).get(0).equals("Forecast Category")) {
                ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).dropDownCheckOpportunity(2, data.get(i).get(1));
            } else if (data.get(i).get(0).equals("Lead Source")) {
                ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).dropDownCheckOpportunity(4, data.get(i).get(1));
            } else if (data.get(i).get(0).equals("Stage")) {
                ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).dropDownCheckOpportunity(1, data.get(i).get(1));
            }
        }
    }

    @When("I select current dates for the below")
    public void i_select_current_dates_for_the_below(DataTable dataTable1) throws InterruptedException {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        Thread.sleep(3000);
        List<List<String>> data = dataTable1.cells();
        for (int i = 0; i < dataTable1.cells().size(); i++) {
            if (data.get(i).get(0).equals("CloseDate")) {
                ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).closeDateValidation();
            }
        }

    }

    @When("I open the newly created link")
    public void i_open_the_newly_created_link() throws InterruptedException {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).opportunityLinkSelection();
    }

    @When("I click on the {string} User search {string} select {string} from the search lists")
    public void i_click_on_the_user_search_select_from_the_search_lists(String string, String string2, String string3) throws InterruptedException {
        Pages page = Pages.getPagesEnumFromPageRef("Generic Page");
        setPage(page);
        ScenarioContext.currentPage.getPageRef();
        ScenarioContext.currentPage.castPagesAs(GenericFunctionsPage.class).appManager(string2);

    }
}