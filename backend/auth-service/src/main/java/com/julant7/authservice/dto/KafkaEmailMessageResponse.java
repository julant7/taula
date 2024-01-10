package com.julant7.authservice.dto;

public record KafkaEmailMessageResponse (
        String email,
        String message
){
}
