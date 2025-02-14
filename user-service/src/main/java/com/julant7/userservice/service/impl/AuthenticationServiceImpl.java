package com.julant7.userservice.service.impl;

import com.julant7.userservice.dto.*;
import com.julant7.userservice.exception.*;
import com.julant7.userservice.model.Jwt;
import com.julant7.userservice.model.User;
import com.julant7.userservice.repository.JwtRepository;
import com.julant7.userservice.repository.UserRepository;
import com.julant7.userservice.service.AuthenticationService;
import com.julant7.userservice.service.JwtService;
import com.julant7.userservice.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JwtRepository jwtRepository;
    private final AuthenticationManager authenticationManager;
    private final String jwtErrorMessage = "Invalid JWT Token";
    @Override
    public JwtAuthenticationResponse login(SignInRequest signInRequest) {
        String username, credential = signInRequest.getCredential();
        User user;
        if (credential.contains("@")) {
            user = userRepository.findByEmail(credential).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
            username = user.getUsername();
        }
        else {
            user = userRepository.findByUsername(credential).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
            username = credential;
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                    signInRequest.getPassword()));
        }
        catch (AuthenticationException e) {
            throw new MyAuthenticationException(e.getMessage());
        }
        String refreshToken = jwtService.generateRefreshToken(user);
        String accessToken = jwtService.generateAccessToken(user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setAccessToken(accessToken);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }

    @Override
    public String logout(String token) throws JwtException {
        if (jwtService.isTokenValid(token)) {
            SecurityContextHolder.getContext().setAuthentication(null);
            String userId = jwtService.extractUserId(token);
            String jwtId = jwtService.extractJwtId(token);
            if (userId == null || jwtId == null) throw new JwtException(jwtErrorMessage);
            Optional<Jwt> jwt = jwtRepository.findByUserId(Long.valueOf(userId));
            if (jwt.isPresent()) {
                boolean isDeleted = jwt.get().getJwtId().remove(UUID.fromString(jwtId));
                if (!isDeleted) throw new JwtException(jwtErrorMessage);
                jwtRepository.save(jwt.get());
            }
            return "Logout successful!";
        }
        throw new JwtException(jwtErrorMessage);
    }

    @Transactional
    @Override
    public JwtAuthenticationResponse refreshToken(@NonNull String refreshToken) throws JwtException {
        if (jwtService.isTokenValid(refreshToken)) {
            String userId = jwtService.extractUserId(refreshToken);
            String jwtId = jwtService.extractJwtId(refreshToken);
            if (userId == null || jwtId == null) throw new JwtException(jwtErrorMessage);
            Jwt jwt = jwtRepository.findByUserId(Long.valueOf(userId)).orElseThrow(() -> new JwtException(jwtErrorMessage));
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new JwtException(jwtErrorMessage));
            boolean isDeleted = jwt.getJwtId().remove(UUID.fromString(jwtId));
            if (!isDeleted) throw new JwtException(jwtErrorMessage);
            jwtRepository.save(jwt);
            String newRefreshToken = jwtService.generateRefreshToken(user);
            String newAccessToken = jwtService.generateAccessToken(user);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setRefreshToken(newRefreshToken);
            jwtAuthenticationResponse.setAccessToken(newAccessToken);
            return jwtAuthenticationResponse;
        }
        throw new JwtException(jwtErrorMessage);
    }

    @Override
    public GenerateQRUrlResponse generateQRUrl(String token) throws UnsupportedEncodingException, JwtException {
        log.info("hiiiiii");
        if (jwtService.isTokenValid(token)) {
            log.info("not okay");
            String userId = jwtService.extractUserId(token);
            if (userId == null) throw new JwtException(jwtErrorMessage);
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new JwtException(jwtErrorMessage));
            return new GenerateQRUrlResponse(
                    URLEncoder.encode(String.format(
                                    "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                                    "Taula", user.getEmail(), user.getSecret(), "Taula"),
                            StandardCharsets.UTF_8)
            );
        }
        throw new JwtException(jwtErrorMessage);
    }

    @Override
    public boolean isValidVerificationCode(User user, String verificationCode) {
        Totp totp = new Totp(user.getSecret());
        return isValidLong(verificationCode) && totp.verify(verificationCode);
    }

    private boolean isValidLong(String code) {
        try {
            Long.parseLong(code);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
