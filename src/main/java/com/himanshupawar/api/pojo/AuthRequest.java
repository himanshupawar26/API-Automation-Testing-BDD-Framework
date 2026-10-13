package com.himanshupawar.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request payload for /register and /login. Fields are nullable on purpose:
 * negative-path scenarios (e.g. "register with missing password") construct
 * this with one field deliberately left null, and JsonInclude.Include.NON_NULL
 * ensures that field is omitted from the serialized JSON entirely, rather than
 * sent as a literal "null" string.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthRequest {

    private String email;
    private String password;

    public static AuthRequest of(String email, String password) {
        AuthRequest req = new AuthRequest();
        req.setEmail(email);
        req.setPassword(password);
        return req;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
