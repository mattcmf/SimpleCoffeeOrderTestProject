Feature: Login
    Perform login on email and password are inputted


  Scenario Outline: Verify the notification panel is displayed
    Given I have a CoffeeOrderActivity
      When I add '3' Espressos
      Then I pay for the order
       Then I should receive a notification that an order has been placed

    Examples:
      | email |
      | test  |
