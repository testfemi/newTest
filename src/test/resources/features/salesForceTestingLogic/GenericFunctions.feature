Feature: Test to verify user can login successfully
  Background:
    Given the user navigates to the "Login Page" page
    When the user enters the "Finance" user login credentials
    And I click the Login button

  @Salesforce1
  Scenario: Test to verify User can Login with the correct Username and Password
    When the user enters the "Finance" user login credentials
    And I click the Login button
    Then the "Fundraising" user is logged in successfully

  @Salesforce2
  Scenario: Test to Select Generic Scenarios in regards user profile
    When I click on the "View Profile" from the top Right hand Menu Bar
    And  I Click "User Profile" from the dropdown profile list
    And  I click on the "User Detail" from the middle right hand Menu
    Then the following permission sets are visible:
      | Fundraising User           |
      | Supporter Care Team Member |

    And I click on the "View Profile" from the top Right hand Menu Bar
    And  I Click "User Profile" from the dropdown profile list
    And I click "Edit" from the middle right hand Menu
    And I enter the following onto the below page:
      | Title      | Doctor |
      | First Name | David  |
      | Last Name  | Qwerty |
      | Suffix     | Mr     |
    And I click "Save" Button
    And I click on the "View Profile" from the top Right hand Menu Bar
    And I Click "Log Out" from the dropdown profile list!


  @Salesforce3
  Scenario: Test to Select Generic Scenarios in regards user Test Profile
    When I click on the "View Profile" from the top Right hand Menu Bar
    And  I Click "User Profile" from the dropdown profile list
    And  I click on the "Edit" from the middle right hand Menu
    And I enter the following onto the below page:
      | Last Name | ABC |
      | Title    | ABC |
      | Suffix    | ABC |
      | Middle Name    | Mark |


    And I click "Save" Button

  @Salesforce4
  Scenario: Test to Select Generic Scenarios in regards user journey
    When  I click on "Contacts" from the top Menu Bar
    And   I click on "Quotes" from the top Menu Bar
    And   I click on "Reports" from the top Menu Bar
    And   I click on "Home" from the top Menu Bar
    And   I click on "Contacts" from the top Menu Bar and select "New Contact" from the dropbox
    And   I click "Cancel" Button
    And   I click on "Contacts" from the top Menu Bar
    And   I click "New" Button
    And   I enter the following onto the below page:
          | First Name   |  Paul|
          | Last Name | Davies |
          | Title    | ABC |
    And   I click "Save" Button
    And   I click on the Pencil Icon field next to "Title" to edit
    And   I enter the following onto the below page:
          | First Name   |  Mark|
          | Last Name | White |
          | Title    | Mr |
    And   I click "Save" Button
    And   I click on the "Activity"
    And   I click on the "Sales"
    And   I click on the "Details"
    And   I click on the Pencil Icon field next to "Department" to edit
    And   I enter the following onto the below page:
          | Mobile            |  369852       |
          | Department        | Accounting    |
          | Mailing Street    | Address Leeds |
          | Reports To        | Mark Davis    |
    And   I click "Save" Button
    And   I click on "Contacts" from the top Menu Bar
    And   I select a contact from the list of Contacts
    And   I click "Opportunity" Link from the right hand side
    And   I click "New" Button for Opportunities
    #And   I click "New" Button on the right hand side
    And   I enter the following onto the below page:
          | Account Name      |  Femi Adeyinka|
          | Opportunity Name  | Accounting    |
    And   I select the following from the dropdwon lists
          | Forecast Category      |  Omitted |
          | Lead Source  | Other    |
          | Stage      | Qualification    |
    And   I select current dates for the below
          | Close Date      |  Current Date |
    And   I click "Save" Button
    And   I open the newly created link
    And   I click on the "Proposal"
    And   I click on the "Negotiation"



  @Salesforce5
  Scenario: Test to Select Generic Scenarios from App Launcher
    When I click on the "App launcher" User search "Accounts" select "Accounts" from the search lists
    And  I click on the "Sales" User search "Sales" select "Sales" from the search lists
    And  I click on the "Opportunities launcher" User search "Opportunities" select "Opportunities" from the search lists

  @Salesforce6
  Scenario: Test to Select Generic Scenarios from App Launcher and create an account
    When I click on the "App launcher" User search "Accounts" select "Accounts" from the search lists
    And  I click on the "New" from the middle right hand Menu
    And I enter the following onto the below page:
      |  Account Name     |   Auto??????                |
      |  State            |  Femi                       |
      |  Type             |  Analyst                    |
      |  Website          |  www.salesforce.com         |
      |  Employees        |  5                          |
      |  Billing Street   |  Leeds                      |
      |  Claim Value      |  500                        |
      |  Date of Receipt  |  "Current Date"             |
      |  Repay by Date    |  "Current Date +100"        |

    And  I click "Save" to save the records
    And  I click on "Contacts" from the Menu
    And  I click Any contact link from the contacts list
    Then the data is saved

  @Salesforce7
  Scenario: Test to search account using the global search field
    When I click on the using the global search field
    And  User search "Sales"
    And  select "Femi Adeyinka" from the search lists with "Accounts"
    When I click on the using the global search field
    And  User search "Femi Adeyinka"
    And  select "Femi Adeyinka" from the search lists with "Contacts"


  @Salesforce8
  Scenario: Test to open a contact and use the pencil icon to edit
    When I click on the "App launcher" User search "Contacts" select "Contacts" from the search lists
    And  I select A contact from the contact lists
    And  I click on the pencil Icon to Amend the following
      |  Middle Name      |   Paul                |
      |  Salutation       |  Mr                   |
      |  Type             |  Analyst              |
      |  Website          |  www.salesforce.com   |
    And I click "Save" Button

  @Salesforce44
  Scenario: Test to Select Generic Scenarios in regards user journey
    When  I click on "Contacts" from the top Menu Bar
    And   I click on "Quotes" from the top Menu Bar
    And   I click on "Reports" from the top Menu Bar
    And   I click on "Home" from the top Menu Bar
    And   I click on "Contacts" from the top Menu Bar and select "New Contact" from the dropbox
    And   I click "Cancel" Button
    And   I click on "Contacts" from the top Menu Bar
    And   I click "New" Button
    And   I enter the following onto the below page:
      | First Name   |  Paul|
      | Last Name | Davies |
      | Title    | ABC |
    And   I click "Save" Button from Contact
    And   I click on the Pencil Icon field next to "Title" to edit
    And   I enter the following onto the below page:
      | First Name   |  Mark|
      | Last Name | White |
      | Title    | Mr |
    And   I click "Save" Button from Contact
    And   I click on the "Activity"
    And   I click on the "Sales"
    And   I click on the "Details"
    And   I click on the Pencil Icon field next to "Department" to edit
    And   I enter the following onto the below page:
      | Mobile            |  369852       |
      | Department        | Accounting    |
      | street            | Address Leeds |
      | Reports To        | Testing Scenario    |
    And   I click "Save" Button from Contact
    And   I click on "Contacts" from the top Menu
    And   I select a contact from the list of Contacts
    And   I click "Opportunities" Link from the right hand side
    And   I click "New" Button for Opportunities
    And   I enter the following onto the below page:
      | Account Name      |  Femi Adeyinka|
      | Opportunity Name  | Accounting    |
    And   I select the following from the dropdwon lists
      | Forecast Category      |  Omitted |
      | Lead Source  | Other    |
      | Stage      | Qualification    |
    And   I select current dates for the below
      | CloseDate      |  Current Date |
    And   I click "Save" Button for Opportunities
    And   I open the newly created link
    And   I click on the "Proposal"
    And   I click on the "Negotiation"
    When I click on the "App launcher" User search "Accounts" select "Accounts" from the search lists
    And  I click on the "Sales" User search "Sales" select "Sales" from the search lists
    And  I click on the "Opportunities launcher" User search "Opportunities" select "Opportunities" from the search lists
    And I click on the "View Profile" from the top Right hand Menu Bar
    And I Click "Log Out" from the dropdown profile list!








