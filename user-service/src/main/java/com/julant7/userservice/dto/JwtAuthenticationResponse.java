package com.julant7.userservice.dto;

public class JwtAuthenticationResponse {
    private String refreshToken;
    private String accessToken;
    public JwtAuthenticationResponse() {

    }
    public JwtAuthenticationResponse(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
