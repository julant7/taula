package com.julant7.mailservice.utils;

public record KafkaMailMessage(
        String email,
        String message
) {
}
