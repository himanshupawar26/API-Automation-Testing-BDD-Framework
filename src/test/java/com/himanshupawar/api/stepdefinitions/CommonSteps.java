package com.himanshupawar.api.stepdefinitions;

import com.himanshupawar.api.hooks.TestContext;
import io.cucumber.java.en.Then;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions shared across every feature file (status code assertion
 * is used by both users.feature and auth.feature). Keeping it here once
 * avoids duplicate-step-definition errors from Cucumber and avoids the
 * copy-paste drift that caused subtle bugs in the old framework.
 */
public class CommonSteps {

    private final TestContext context;

    public CommonSteps(TestContext context) {
        this.context = context;
    }

    @Then("the response status code is {int}")
    public void the_response_status_code_is(int expectedStatusCode) {
        int actual = context.getLastResponse().statusCode();
        assertThat(actual)
                .as("expected status %d but got %d. Response body: %s",
                        expectedStatusCode, actual, context.getLastResponse().getBody().asString())
                .isEqualTo(expectedStatusCode);
    }
}
