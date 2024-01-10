package com.julant7.authservice.service;

import com.julant7.authservice.exception.Base64OperationException;

public interface Base64Service {
    <T> T decode(String data, Class<T> to) throws Base64OperationException;
    <T> String encode(T t) throws Base64OperationException;
}
