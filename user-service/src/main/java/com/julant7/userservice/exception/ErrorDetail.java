package com.julant7.userservice.exception;

import java.util.Date;

public class ErrorDetail {
    private String error;
    private String message;
    private Date timeStamp;

    public ErrorDetail() {

    }

    public ErrorDetail(String error, String message, Date timeStamp) {
        this.error = error;
        this.message = message;
        this.timeStamp = timeStamp;
    }
}

