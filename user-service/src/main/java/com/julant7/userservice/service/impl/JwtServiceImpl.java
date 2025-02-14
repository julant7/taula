package com.julant7.userservice.service.impl;

import com.julant7.userservice.dto.SignUpRequest;
import com.julant7.userservice.model.Jwt;
import com.julant7.userservice.model.User;
import com.julant7.userservice.repository.JwtRepository;
import com.julant7.userservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.lifetimeForRefreshToken}")
    private long lifetimeForRefreshToken;

    @Value("${security.jwt.lifetimeForAccessToken}")
    private long lifetimeForAccessToken;

    @Value("${security.jwt.lifetimeForCreateUser}")
    private long lifetimeForCreateUser;

    @Value("${security.jwt.lifetimeForResetPassword}")
    private long lifetimeForResetPassword;
    private final Key jwtSecret;
    private final JwtRepository jwtRepository;

    public JwtServiceImpl(
            JwtRepository jwtRepository,
            @Value("${security.jwt.secret}") String jwtSecret
    ) {
        this.jwtSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        this.jwtRepository = jwtRepository;
    }
    @Override
    public String generateRefreshToken(@NonNull User user) {
        // Generating a token for a long time
        UUID jwtId = UUID.randomUUID();
        Long userId = user.getId();
        Jwt jwt = new Jwt();
        jwt.setJwtId(jwtId);
        jwt.setUserId(userId);
        jwtRepository.save(jwt);
        return Jwts.builder()
                .id(jwtId.toString())
                .subject(userId.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + lifetimeForRefreshToken))
                .signWith(jwtSecret)
                .compact();
    }

    @Override
    public String generateAccessToken(@NonNull User user) {
        // Generating a token for short periods of time
        UUID jwtId = UUID.randomUUID();
        Long userId = user.getId();
        return Jwts.builder()
                .id(jwtId.toString())
                .subject(userId.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + lifetimeForAccessToken))
                .signWith(jwtSecret)
                .compact();
    }

    @Override
    public String generateTokenForRegistration(SignUpRequest signUpRequest) {
        return Jwts.builder()
                .claim("firstName", signUpRequest.getFirstName())
                .claim("lastName", signUpRequest.getLastName())
                .claim("email", signUpRequest.getEmail())
                .claim("username", signUpRequest.getUsername())
                .claim("work", signUpRequest.getWork())
                .claim("password", signUpRequest.getPassword())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + lifetimeForCreateUser))
                .signWith(jwtSecret)
                .compact();
    }

    @Override
    public String generateTokenForResetPassword(Long userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + lifetimeForResetPassword))
                .signWith(jwtSecret)
                .compact();
    }

    @Override
    public String generateTokenForUpdateEmail(Long userId, String newEmail) {
        return Jwts.builder()
                .claim("userId", userId.toString())
                .claim("newEmail", newEmail)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + lifetimeForResetPassword))
                .signWith(jwtSecret)
                .compact();
    }

    @Override
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String extractJwtId(String token) {
        return extractClaim(token, Claims::getId);
    }

    @Override
    public Claims extractALlClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractALlClaims(token);
        return claimsResolvers.apply(claims);
    }

    @Override
    public boolean isAccessTokenValid(String token) {
        try {
            extractALlClaims(token);
            String userId = extractUserId(token);
            return (userId != null && !isTokenExpired(token));
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isRefreshTokenValid(String token) {
        try {
            String userId = extractUserId(token);
            String jwtId = extractJwtId(token);
            if (userId == null || jwtId == null) return false;
            Optional<Jwt> jwt = jwtRepository.findByUserId(Long.valueOf(userId));
            if (jwt.isPresent())
                return jwt.get().getJwtId().contains(UUID.fromString(jwtId)) && !isTokenExpired(token);
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isTokenValid(@NonNull String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) jwtSecret)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid token 1111");
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
