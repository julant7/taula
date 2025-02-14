package com.julant7.userservice.service;

import com.julant7.userservice.dto.*;
import com.julant7.userservice.exception.*;
import com.julant7.userservice.model.User;
import org.springframework.lang.NonNull;

import java.io.UnsupportedEncodingException;

public interface AuthenticationService {
    JwtAuthenticationResponse login(SignInRequest signInRequest);

    String logout(String token) throws JwtException;

    JwtAuthenticationResponse refreshToken(@NonNull String refreshToken) throws JwtException;

    GenerateQRUrlResponse generateQRUrl(String token) throws UnsupportedEncodingException, JwtException;

    boolean isValidVerificationCode(User user, String verificationCode);
}
