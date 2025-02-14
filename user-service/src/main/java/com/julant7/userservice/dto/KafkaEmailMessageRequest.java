package com.julant7.userservice.dto;

public record KafkaEmailMessageRequest(
        String email,
        String message
){
}
