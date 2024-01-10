package com.julant7.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetail> userExceptionHandler(UserException e, WebRequest req) {

        ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), System.currentTimeMillis() / 1000L);
        return new ResponseEntity<ErrorDetail>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Base64OperationException.class)
    public ResponseEntity<ErrorDetail> userExceptionHandler(Base64OperationException e, WebRequest req) {

        ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), System.currentTimeMillis() / 1000L);
        return new ResponseEntity<ErrorDetail>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LinkExpiredException.class)
    public ResponseEntity<ErrorDetail> userExceptionHandler(LinkExpiredException e, WebRequest req) {

        ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), System.currentTimeMillis() / 1000L);
        return new ResponseEntity<ErrorDetail>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetail> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, WebRequest req) {
        ErrorDetail err = new ErrorDetail("Validation error", req.getDescription(false), System.currentTimeMillis() / 1000L);
        return new ResponseEntity<ErrorDetail>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetail> handleNoHandlerFoundException(NoHandlerFoundException e, WebRequest req) {
        ErrorDetail err = new ErrorDetail("Endpoint not found", req.getDescription(false), System.currentTimeMillis() / 1000L);
        return new ResponseEntity<ErrorDetail>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> otherExceptionHandler(Exception e, WebRequest req) {
        ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), System.currentTimeMillis() / 1000L);
        return new ResponseEntity<ErrorDetail>(err, HttpStatus.BAD_REQUEST);
    }
}

