package com.himanshupawar.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Shape of ReqRes's error responses, e.g.
 *   { "error": "Missing password" }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
