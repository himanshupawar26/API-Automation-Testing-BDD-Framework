package com.himanshupawar.api.clients;

import com.himanshupawar.api.pojo.User;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Thin wrapper over the /users resource. Step definitions call these methods
 * instead of building RestAssured requests inline, so request construction
 * lives in one place per resource and step defs stay readable as behavior,
 * not HTTP plumbing.
 */
public class UsersClient extends BaseClient {

    private final String baseUrl;

    public UsersClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response list(int page) {
        return given().spec(requestSpec(baseUrl))
                .queryParam("page", page)
                .when()
                .get("/users");
    }

    public Response getById(String id) {
        return given().spec(requestSpec(baseUrl))
                .when()
                .get("/users/{id}", id);
    }

    public Response create(User user) {
        return given().spec(requestSpec(baseUrl))
                .body(user)
                .when()
                .post("/users");
    }

    public Response update(String id, User user) {
        return given().spec(requestSpec(baseUrl))
                .body(user)
                .when()
                .put("/users/{id}", id);
    }

    public Response partialUpdate(String id, User user) {
        return given().spec(requestSpec(baseUrl))
                .body(user)
                .when()
                .patch("/users/{id}", id);
    }

    public Response delete(String id) {
        return given().spec(requestSpec(baseUrl))
                .when()
                .delete("/users/{id}", id);
    }
}
