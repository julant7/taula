package com.julant7.authservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.julant7.authservice.exception.Base64OperationException;
import com.julant7.authservice.service.Base64Service;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class Base64ServiceImpl implements Base64Service {
    private final ObjectMapper objectMapper;

    public Base64ServiceImpl(
            @Qualifier("customObjectMapper") ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
    }
    @Override
    public <T> T decode(String data, Class<T> to) throws Base64OperationException {
        try {
            byte[] decodeBytes = Base64.getDecoder().decode(data);
            String jsonData = new String(decodeBytes);
            return objectMapper.readValue(jsonData, to);
        }
        catch (Exception e) {
            throw new Base64OperationException("Failed to decode or convert the data");
        }
    }

    @Override
    public <T> String encode(T t) throws Base64OperationException {
        String jsonData = null;
        try {
            jsonData = objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new Base64OperationException("Failed to decode");
        }
        return Base64.getEncoder().encodeToString(jsonData.getBytes());
    }
}
