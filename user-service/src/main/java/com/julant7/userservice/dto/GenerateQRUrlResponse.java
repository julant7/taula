package com.julant7.userservice.dto;

public class GenerateQRUrlResponse {
    private String url;

    public GenerateQRUrlResponse(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
