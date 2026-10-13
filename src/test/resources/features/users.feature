Feature: Users resource
  As an API consumer of the ReqRes service
  I want to list, create, update, and delete users
  So that I can manage user records programmatically

  # Note on ReqRes's write endpoints: ReqRes is a read-only demo API. GET /users
  # and GET /users/{id} return real, persistent fixture data (12 users across
  # 2 pages). POST/PUT/PATCH/DELETE are accepted and return a realistic response
  # (echoing the payload back with a generated id/timestamp), but nothing is
  # actually persisted server-side — a subsequent GET will not reflect the write.
  # These scenarios validate the request/response *contract* of the write
  # endpoints (status codes, response shape, field echoing), not persistence.

  @smoke @regression @users
  Scenario: List users returns a paginated result with the expected schema
    When I request page 2 of the users list
    Then the response status code is 200
    And the response contains pagination fields "page", "per_page", "total", "total_pages"
    And each user in the response has fields "id", "email", "first_name", "last_name", "avatar"

  @regression @users
  Scenario: Get an existing user by id returns that user's details
    When I request the user with id "2"
    Then the response status code is 200
    And the user's email in the response ends with "@reqres.in"

  @regression @users @negative
  Scenario: Get a non-existent user returns 404 with an empty object body
    When I request the user with id "999"
    Then the response status code is 404
    And the response body is an empty JSON object

  @smoke @regression @users
  Scenario: Creating a user returns the created resource with a generated id
    When I create a user named "Himanshu Pawar" with job "SDET"
    Then the response status code is 201
    And the created user's name is "Himanshu Pawar" and job is "SDET"
    And the response contains a generated "id" and "createdAt" timestamp

  @regression @users
  Scenario: Fully updating a user returns the updated fields
    Given a user named "Himanshu Pawar" with job "SDET" has been created
    When I update that user's job to "Senior SDET"
    Then the response status code is 200
    And the updated user's job is "Senior SDET"
    And the response contains an "updatedAt" timestamp

  @regression @users
  Scenario: Partially updating a user only changes the given field
    Given a user named "Himanshu Pawar" with job "SDET" has been created
    When I partially update that user's job to "Lead SDET"
    Then the response status code is 200
    And the updated user's job is "Lead SDET"

  @smoke @regression @users
  Scenario: Deleting a user returns 204 with no content
    Given a user named "Himanshu Pawar" with job "SDET" has been created
    When I delete that user
    Then the response status code is 204
    And the response body is truly empty
