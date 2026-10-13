package com.himanshupawar.api.clients;

import com.himanshupawar.api.pojo.AuthRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Thin wrapper over the /register and /login resources.
 */
public class AuthClient extends BaseClient {

    private final String baseUrl;

    public AuthClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response register(AuthRequest request) {
        return given().spec(requestSpec(baseUrl))
                .body(request)
                .when()
                .post("/register");
    }

    public Response login(AuthRequest request) {
        return given().spec(requestSpec(baseUrl))
                .body(request)
                .when()
                .post("/login");
    }
}
