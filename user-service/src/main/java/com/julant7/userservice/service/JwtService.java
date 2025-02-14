package com.julant7.userservice.service;

import com.julant7.userservice.dto.SignUpRequest;
import com.julant7.userservice.model.User;
import io.jsonwebtoken.Claims;
import org.springframework.lang.NonNull;

public interface JwtService {
    String generateTokenForRegistration(SignUpRequest signUpRequest);

    String generateTokenForResetPassword(Long userId);

    String generateTokenForUpdateEmail(Long userId, String newEmail);

    String extractUserId(String token);

    String extractJwtId(String token);

    String generateAccessToken(@NonNull User user);

    String generateRefreshToken(@NonNull User user);

    Claims extractALlClaims(String token);

    boolean isRefreshTokenValid(String token);

    boolean isAccessTokenValid(String token);

    boolean isTokenValid(@NonNull String token);

}
