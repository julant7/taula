package com.julant7.authservice.exception;

import java.time.LocalDateTime;

public class ErrorDetail {
    private String error;
    private String message;
    private Long timeStamp;

    public ErrorDetail() {

    }

    public ErrorDetail(String error, String message, Long timeStamp) {
        this.error = error;
        this.message = message;
        this.timeStamp = timeStamp;
    }
}

