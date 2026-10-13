package com.himanshupawar.api.stepdefinitions;

import com.himanshupawar.api.clients.UsersClient;
import com.himanshupawar.api.config.Config;
import com.himanshupawar.api.hooks.TestContext;
import com.himanshupawar.api.pojo.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UsersSteps {

    private final TestContext context;
    private final UsersClient usersClient;

    private String pendingUserName;
    private String pendingUserJob;

    public UsersSteps(TestContext context) {
        this.context = context;
        this.usersClient = new UsersClient(Config.baseUrl());
    }

    @When("I request page {int} of the users list")
    public void i_request_page_of_the_users_list(int page) {
        context.setLastResponse(usersClient.list(page));
    }

    @When("I request the user with id {string}")
    public void i_request_the_user_with_id(String id) {
        context.setLastResponse(usersClient.getById(id));
    }

    @When("I create a user named {string} with job {string}")
    public void i_create_a_user_named_with_job(String name, String job) {
        context.setLastResponse(usersClient.create(userPayload(name, job)));
        captureCreatedUserId();
    }

    @Given("a user named {string} with job {string} has been created")
    public void a_user_named_with_job_has_been_created(String name, String job) {
        this.pendingUserName = name;
        this.pendingUserJob = job;
        context.setLastResponse(usersClient.create(userPayload(name, job)));
        captureCreatedUserId();
        assertThat(context.getLastResponse().statusCode())
                .as("Precondition setup (create user) must succeed before continuing the scenario")
                .isEqualTo(201);
    }

    @When("I update that user's job to {string}")
    public void i_update_that_user_s_job_to(String newJob) {
        context.setLastResponse(usersClient.update(context.getCreatedUserId(), userPayload(pendingUserName, newJob)));
    }

    @When("I partially update that user's job to {string}")
    public void i_partially_update_that_user_s_job_to(String newJob) {
        User partial = new User();
        partial.setJob(newJob);
        context.setLastResponse(usersClient.partialUpdate(context.getCreatedUserId(), partial));
    }

    @When("I delete that user")
    public void i_delete_that_user() {
        context.setLastResponse(usersClient.delete(context.getCreatedUserId()));
    }

    @Then("the response contains pagination fields {string}, {string}, {string}, {string}")
    public void the_response_contains_pagination_fields(String f1, String f2, String f3, String f4) {
        JsonPath json = context.getLastResponse().jsonPath();
        for (String field : List.of(f1, f2, f3, f4)) {
            Object value = json.get(field);
            assertThat(value).as("expected pagination field '%s' to be present", field).isNotNull();
        }
    }

    @Then("each user in the response has fields {string}, {string}, {string}, {string}, {string}")
    public void each_user_in_the_response_has_fields(String f1, String f2, String f3, String f4, String f5) {
        List<String> expectedFields = List.of(f1, f2, f3, f4, f5);
        List<java.util.Map<String, Object>> users = context.getLastResponse().jsonPath().getList("data");
        assertThat(users).as("expected at least one user in the list response").isNotEmpty();
        for (java.util.Map<String, Object> user : users) {
            for (String field : expectedFields) {
                assertThat(user).as("user %s missing expected field '%s'", user, field).containsKey(field);
            }
        }
    }

    @Then("the user's email in the response ends with {string}")
    public void the_user_s_email_in_the_response_ends_with(String suffix) {
        String email = context.getLastResponse().jsonPath().getString("data.email");
        assertThat(email).endsWith(suffix);
    }

    @Then("the created user's name is {string} and job is {string}")
    public void the_created_user_s_name_is_and_job_is(String name, String job) {
        JsonPath json = context.getLastResponse().jsonPath();
        assertThat(json.getString("name")).isEqualTo(name);
        assertThat(json.getString("job")).isEqualTo(job);
    }

    @Then("the response contains a generated {string} and {string} timestamp")
    public void the_response_contains_a_generated_id_and_timestamp(String idField, String timestampField) {
        JsonPath json = context.getLastResponse().jsonPath();
        assertThat(json.getString(idField)).as("expected a generated '%s'", idField).isNotBlank();
        assertThat(json.getString(timestampField)).as("expected a generated '%s'", timestampField).isNotBlank();
    }

    @Then("the updated user's job is {string}")
    public void the_updated_user_s_job_is(String job) {
        assertThat(context.getLastResponse().jsonPath().getString("job")).isEqualTo(job);
    }

    @Then("the response contains an {string} timestamp")
    public void the_response_contains_a_timestamp(String timestampField) {
        assertThat(context.getLastResponse().jsonPath().getString(timestampField)).isNotBlank();
    }

    @Then("the response body is an empty JSON object")
    public void the_response_body_is_an_empty_json_object() {
        String body = context.getLastResponse().getBody().asString().trim();
        assertThat(body).isEqualTo("{}");
    }

    @Then("the response body is truly empty")
    public void the_response_body_is_truly_empty() {
        String body = context.getLastResponse().getBody().asString();
        assertThat(body).isEmpty();
    }

    private User userPayload(String name, String job) {
        User user = new User();
        user.setName(name);
        user.setJob(job);
        return user;
    }

    private void captureCreatedUserId() {
        String id = context.getLastResponse().jsonPath().getString("id");
        if (id != null) {
            context.setCreatedUserId(id);
        }
    }
}
