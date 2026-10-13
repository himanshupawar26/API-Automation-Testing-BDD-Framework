package com.himanshupawar.api.hooks;

import io.restassured.response.Response;

/**
 * Per-scenario state shared across step definition classes.
 *
 * The old framework used a `static String placeId` field on a step def class
 * to pass data between steps/scenarios, which is a shared-mutable-state bug
 * waiting to happen under parallel execution (two scenarios racing on the same
 * static field). Cucumber creates one instance of each glue class per scenario
 * via its dependency-injection container (PicoContainer, used implicitly here),
 * so injecting this class instead gives every scenario its own isolated instance.
 */
public class TestContext {

    private Response lastResponse;
    private String createdUserId;
    private String authToken;

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
