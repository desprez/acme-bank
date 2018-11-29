Feature: Domain Account operations

  Scenario: Account Withdrawals is in credit
    Given An account "FR1194431796668004498223348" with Currency is "EUR"
    And a balance of 200
    When I withdraw 100 "EUR"
    Then Account "FR1194431796668004498223348" has balance 100

  Scenario: Account Withdrawals fails with Insufficient funds
    Given An account "FR1194431796668004498223348" with Currency is "EUR"
    And a balance of 200
    When I withdraw 250 "EUR"
    Then it Fails with InsufficientBalanceException
    And Account "FR1194431796668004498223348" has balance 200

  Scenario: Account Withdrawals below lower limit success with negative balance
    Given An account "FR1194431796668004498223348" with lower limit 100 "EUR"
    And a balance of 200
    When I withdraw 250 "EUR"
    And Account "FR1194431796668004498223348" has balance -50

  Scenario: Account deposit is in credit
    Given An account "FR1194431796668004498223348" with Currency is "EUR"
    And a balance of 100
    When I deposit 100 "EUR"
    Then Account "FR1194431796668004498223348" has balance 200

  Scenario: Account deposit in USD fails for account in EUR
    Given An account "FR1194431796668004498223348" with Currency is "EUR"
    And a balance of 100
    When I deposit 100 "USD"
    Then it Fails with MonetaryException
    And Account "FR1194431796668004498223348" has balance 100

  Scenario: Account Withdrawals on suspended account fails with Invalid Account Status
    Given An account "FR1194431796668004498223348" with Currency is "EUR"
    And a balance of 100
    And account is suspended
    When I withdraw 100 "EUR"
    Then it Fails with InvalidAccountStatusException
    And Account "FR1194431796668004498223348" has balance 100

  Scenario: Account Deposit on closed account fails with Invalid Account Status
    Given An account "FR1194431796668004498223348" with Currency is "EUR"
    And account is closed
    When I deposit 100 "EUR"
    Then it Fails with InvalidAccountStatusException
    And Account "FR1194431796668004498223348" has balance 0