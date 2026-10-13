package com.himanshupawar.api.clients;

import com.himanshupawar.api.config.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Base for all resource clients. Builds a single shared RequestSpecification
 * per base URL, with request/response logging enabled only when a response
 * validation (e.g. an AssertJ assertion wired through RestAssured's own
 * expect()/then() calls) fails — keeps CI logs readable while still giving
 * full request/response detail when something breaks.
 *
 * Attaches the x-api-key header automatically when Config.apiKey() resolves
 * to a value, to avoid ReqRes's 40-requests/day anonymous rate limit.
 */
public abstract class BaseClient {

    protected final RequestSpecification requestSpec(String baseUrl) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON);

        String apiKey = Config.apiKey();
        if (apiKey != null && !apiKey.isBlank()) {
            builder.addHeader("x-api-key", apiKey);
        }

        return builder.build().log().ifValidationFails(LogDetail.ALL);
    }
}
