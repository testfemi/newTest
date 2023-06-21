Feature: Training Form page

  Background:
    Given the user navigates to the "TrainingForm" page

  @Test1 @TrainingFormPage @dev22
  Scenario: Test to verify user can enter the relevant email to the below Practice form Page
    Given the user selects the "Sports" option from the "Hobbies" checkbox
    And the user selects the "Music" option from the "Hobbies" checkbox
    And the user selects the "Reading" option from the "Hobbies" checkbox
    And the user selects the "Male" option from the "Gender" checkbox

  @Test @TrainingFormPage @dev
  Scenario: Test to verify user can enter the relevant email to the below Practice form Page
    Given the user selects the "Sports" option from the "Hobbies" checkbox
    Then the following text fields are visible:
      | First Name |
      | Last Name  |
      | Email      |

    Given the user selects the "Music" option from the "Hobbies" checkbox
    And the user selects the "Reading" option from the "Hobbies" checkbox
    And the user selects the "Male" option from the "Gender" checkbox