package com.himanshupawar.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * GET /users/{id} wraps the user object in a top-level "data" field:
 *   { "data": { "id": 2, "email": "...", ... }, "support": {...} }
 *
 * This is a different shape from POST/PUT/PATCH /users, which return the
 * user fields at the root with no wrapper. Modeling them as two distinct
 * POJOs (rather than one "flexible" class) keeps each one an honest
 * representation of what that specific endpoint actually returns.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEnvelope {

    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
