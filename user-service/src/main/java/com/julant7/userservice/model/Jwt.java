package com.julant7.userservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@RedisHash(value = "jwts", timeToLive = 86400)
public class Jwt {
    @Id
    private Long id;
    private Long userId;

    // JWT UUIDs are presented as list for the ability of one user to be authenticated on different devices/browsers
    private List<UUID> jwtId = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setJwtId(UUID jwtId) {
        this.jwtId.add(jwtId);
    }
}
