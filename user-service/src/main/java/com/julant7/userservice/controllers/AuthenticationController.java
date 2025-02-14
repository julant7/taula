package com.julant7.userservice.controllers;

import com.julant7.userservice.dto.*;
import com.julant7.userservice.exception.*;
import com.julant7.userservice.service.AuthenticationService;
import com.julant7.userservice.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(authenticationService.login(signInRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) throws JwtException {
        return ResponseEntity.ok(authenticationService.logout(token));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestHeader("Authorization") String authorizationHeader) throws JwtException {
        String token = authorizationHeader.substring(7);
        return ResponseEntity.ok(authenticationService.refreshToken(token));
    }

    @GetMapping("/generate_qr_code")
    public ResponseEntity<GenerateQRUrlResponse> generateQRUrl(@RequestHeader("Authorization") String authorizationHeader) throws UnsupportedEncodingException, JwtException {
        String token = authorizationHeader.substring(7);
        return ResponseEntity.ok(authenticationService.generateQRUrl(token));
    }
}   
