package com.julant7.authservice.dto;

public class SignInRequest {
    private String username;
    private String password;

    public SignInRequest() {

    }

    public SignInRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}
