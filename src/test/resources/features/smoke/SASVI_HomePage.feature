Feature: Test to verify that all necessary information are displayed on Allocation Reassign Page
  Background:

    Given the user navigates to the "Login Page" page
    When the user enters the "sasvi_james" user login credentials
    Then the "sasvi_james" user is logged in successfully

  @HomePage_Test_TC002.1 @Workflow_component_smoke_test_TC003 @Smoke @Regression
  Scenario: As a platform User, I want to verify that all necessary information are displayed on Allocation Reassign Page
    Given the user selects the "Home" button
    And the user is on the "SASVI Home" page
    Then the following text fields are visible:
      | My VAT Investigations  |
      | My SA Investigations   |
      | MIS Reports            |
