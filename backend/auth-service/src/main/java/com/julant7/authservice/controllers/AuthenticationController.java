package com.julant7.authservice.controllers;

import com.julant7.authservice.dto.JwtAuthenticationResponse;
import com.julant7.authservice.dto.RefreshTokenRequest;
import com.julant7.authservice.dto.SignInRequest;
import com.julant7.authservice.dto.SignUpRequest;
import com.julant7.authservice.exception.Base64OperationException;
import com.julant7.authservice.exception.LinkExpiredException;
import com.julant7.authservice.model.User;
import com.julant7.authservice.service.AuthenticationService;
import com.julant7.authservice.utils.StatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<StatusResponse> requestToRegistration(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authenticationService.requestToRegistration(signUpRequest));
    }

    @PostMapping("/confirm_registration")
    public ResponseEntity<User> confirmRegistration(@RequestParam String data) throws LinkExpiredException, Base64OperationException {
        return ResponseEntity.ok(authenticationService.confirmRegistration(data));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(authenticationService.signin(signInRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }
}
