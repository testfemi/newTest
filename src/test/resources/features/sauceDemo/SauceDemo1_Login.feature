Feature: Test to verify user can login successfully with a valid credential
  Background:
    Given the user navigates to the "Login Page" page

  @SauceDemoTest1
  Scenario: Test to verify User can Login with the correct Username and Password
    When the user enters the "Standard" user login credentials
    And I click the Login button
    Then the "Standard" user is logged in
    When the user selects the Highest price from the list of product and add to cart
    #And the user selects the Lowest price from the list of product and add to cart
    #And I click on the Sort dropdown and select "Price (low to high)"
    And I click on the logout button



