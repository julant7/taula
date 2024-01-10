package com.julant7.authservice.exception;

public class LinkExpiredException extends Exception {
    public LinkExpiredException(String message) {
        super(message);
    }
}