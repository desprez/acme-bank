Feature: Testing a Bank Account REST API
  Users should be able to submit GET, PUT and POST requests to a BankAccount resource

  Scenario: Create Account with valid parameters should success
    When User post a new account request with lower limit of 200 "EUR"
    Then return a success status
    And Returned JSON object with account number is not null

  Scenario: Get Balance Account with valid parameters should success
    Given a bank account
    And initial balance is 900 "EUR"
    When a user makes a getBalance request for this account
    Then return a success status
    And Returned JSON object with is 900 "EUR"

  Scenario: Get Balance Account with unknown Account number should fail
    When a user makes a getBalance request for unknown account
    Then return a not found status

  Scenario: Withdrawal money with valid parameters should success
    Given a bank account
    And initial balance is 900 "EUR"
    When a user makes a withdrawal of 100 "EUR"
    Then return a success status
    And the final balance of the account is 800 "EUR"

  Scenario: Withdrawal with unknown Account number should fail
    When a user makes a withdrawal of 100 "EUR" for unknown account
    Then return a not found status

  Scenario: Deposit money with valid parameters should success
    Given a bank account
    And initial balance is 900 "EUR"
    When User makes a deposit of 100 "EUR"
    Then return a success status
    And the final balance of the account is 1000 "EUR"

  Scenario: Deposit with unknown Account number should fail
    When a user makes a deposit of 100 "EUR" for unknown account
    Then return a not found status

  Scenario: Transfer money with valid parameters should success
    Given a bank account
    And initial balance is 900 "EUR"
    And a other bank account
    And the initial balance of the other account is 100 "EUR"
    When User makes a transfer of 100 "EUR"
    Then return a success status
    And the final balance of the account is 800 "EUR"
    And the final balance of the other account is 200 "EUR"
