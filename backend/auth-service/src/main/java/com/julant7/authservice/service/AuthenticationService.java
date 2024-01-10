package com.julant7.authservice.service;

import com.julant7.authservice.dto.JwtAuthenticationResponse;
import com.julant7.authservice.dto.RefreshTokenRequest;
import com.julant7.authservice.dto.SignInRequest;
import com.julant7.authservice.dto.SignUpRequest;
import com.julant7.authservice.exception.Base64OperationException;
import com.julant7.authservice.exception.LinkExpiredException;
import com.julant7.authservice.model.User;
import com.julant7.authservice.utils.StatusResponse;

public interface AuthenticationService {
    StatusResponse requestToRegistration(SignUpRequest signUpRequest);

    User confirmRegistration(String hash) throws LinkExpiredException, Base64OperationException;

    User signup(SignUpRequest signUpRequest);
    JwtAuthenticationResponse signin(SignInRequest signInRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
