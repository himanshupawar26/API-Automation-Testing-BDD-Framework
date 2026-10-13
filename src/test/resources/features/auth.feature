Feature: Authentication
  As an API consumer of the ReqRes service
  I want to register and log in
  So that I can obtain an auth token for authenticated requests

  @smoke @regression @auth
  Scenario: Registering with a valid email and password succeeds
    When I register with email "eve.holt@reqres.in" and password "pistol"
    Then the response status code is 200
    And the response contains a non-empty "token"

  @regression @auth @negative
  Scenario: Registering without a password fails with a 400 and a clear error
    When I register with email "eve.holt@reqres.in" and no password
    Then the response status code is 400
    And the error message is "Missing password"

  @smoke @regression @auth
  Scenario: Logging in with valid credentials succeeds
    When I log in with email "eve.holt@reqres.in" and password "cityslicka"
    Then the response status code is 200
    And the response contains a non-empty "token"

  @regression @auth @negative
  Scenario: Logging in without a password fails with a 400 and a clear error
    When I log in with email "eve.holt@reqres.in" and no password
    Then the response status code is 400
    And the error message is "Missing password"

  @regression @auth @negative
  Scenario: Logging in with an unregistered email fails with a 400
    When I log in with email "unregistered.user@example.com" and password "whatever"
    Then the response status code is 400
    And the error message is "user not found"
