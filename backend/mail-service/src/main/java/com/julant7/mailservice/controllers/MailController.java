//package com.julant7.mailservice.controllers;
//
//import com.julant7.authservice.dto.JwtAuthenticationResponse;
//import com.julant7.authservice.dto.RefreshTokenRequest;
//import com.julant7.authservice.dto.SignInRequest;
//import com.julant7.authservice.dto.SignUpRequest;
//import com.julant7.authservice.exception.Base64OperationException;
//import com.julant7.authservice.exception.LinkExpiredException;
//import com.julant7.authservice.model.User;
//import com.julant7.authservice.service.AuthenticationService;
//import com.julant7.taulu.utils.StatusResponse;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//public class MailController {
//    private final AuthenticationService authenticationService;
//
//    public AuthenticationController(AuthenticationService authenticationService) {
//        this.authenticationService = authenticationService;
//    }
//    @PostMapping("/signup")
//    public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest) {
//        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
//    }