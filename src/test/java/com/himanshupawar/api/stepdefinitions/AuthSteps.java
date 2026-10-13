package com.himanshupawar.api.stepdefinitions;

import com.himanshupawar.api.clients.AuthClient;
import com.himanshupawar.api.config.Config;
import com.himanshupawar.api.hooks.TestContext;
import com.himanshupawar.api.pojo.AuthRequest;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthSteps {

    private final TestContext context;
    private final AuthClient authClient;

    public AuthSteps(TestContext context) {
        this.context = context;
        this.authClient = new AuthClient(Config.baseUrl());
    }

    @When("I register with email {string} and password {string}")
    public void i_register_with_email_and_password(String email, String password) {
        context.setLastResponse(authClient.register(AuthRequest.of(email, password)));
    }

    @When("I register with email {string} and no password")
    public void i_register_with_email_and_no_password(String email) {
        context.setLastResponse(authClient.register(AuthRequest.of(email, null)));
    }

    @When("I log in with email {string} and password {string}")
    public void i_log_in_with_email_and_password(String email, String password) {
        context.setLastResponse(authClient.login(AuthRequest.of(email, password)));
    }

    @When("I log in with email {string} and no password")
    public void i_log_in_with_email_and_no_password(String email) {
        context.setLastResponse(authClient.login(AuthRequest.of(email, null)));
    }

    @Then("the response contains a non-empty {string}")
    public void the_response_contains_a_non_empty_field(String field) {
        String value = context.getLastResponse().jsonPath().getString(field);
        assertThat(value).as("expected non-empty '%s'", field).isNotBlank();
        context.setAuthToken(value);
    }

    @Then("the error message is {string}")
    public void the_error_message_is(String expectedMessage) {
        String actual = context.getLastResponse().jsonPath().getString("error");
        assertThat(actual).isEqualTo(expectedMessage);
    }
}
