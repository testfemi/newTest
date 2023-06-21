Feature: Test to verify user can login successfully with a valid credential and unsuccessful with invalid credentials
  Background:
    Given the user navigates to the "Login Page" page

  @Salesforce101
  Scenario: Test to verify User can Login with the correct Username and Password
    When the user enters the "Finance" user login credentials
    And I click the Login button
    Then the "Fundraising" user is logged in successfully

  @Salesforce102
  Scenario: Test to verify User can Login with the correct Username and Password
    When the user enters the "Income" user login credentials
    And I click the Login button
    Then the "test" user is logged in successfully


  @Salesforce103
  Scenario: Test to verify User cannot Login with the correct Username and incorrect Password
    When the user enters the following onto the page:
      | Username | james |
      | Password | XXXXX |
    Then the user login is unsuccessful


