package com.julant7.userservice.dto;

import java.util.UUID;

public record KafkaJwtSaveResponse(
        UUID jwtId,
        Long userId
) {
}
