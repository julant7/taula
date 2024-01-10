package com.julant7.authservice.service.impl;

import com.julant7.authservice.dto.*;
import com.julant7.authservice.exception.Base64OperationException;
import com.julant7.authservice.exception.LinkExpiredException;
import com.julant7.authservice.exception.UserException;
import com.julant7.authservice.model.Role;
import com.julant7.authservice.model.User;
import com.julant7.authservice.repository.UserRepository;
import com.julant7.authservice.service.AuthenticationService;
import com.julant7.authservice.service.Base64Service;
import com.julant7.authservice.service.JWTService;
import com.julant7.authservice.service.KafkaProducer;
import com.julant7.authservice.utils.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaProducer kafkaProducer;
    private final Base64Service base64;
    public static final String EMAIL_TOPIC = "email_message";

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, KafkaProducer kafkaProducer, Base64Service base64, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaProducer = kafkaProducer;
        this.base64 = base64;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public StatusResponse requestToRegistration(SignUpRequest signUpRequest) {
        try {
            var optionalUser = this.userRepository.findByEmail(signUpRequest.getEmail());
            if (optionalUser.isPresent()) {
                throw new UserException("Email: %s registered yet.".formatted(signUpRequest.getEmail()));
            }
            var dataToSend = base64.encode(signUpRequest);

            kafkaProducer.produce(EMAIL_TOPIC, new KafkaEmailMessageResponse(signUpRequest.getEmail(), dataToSend));
            return new StatusResponse(
                    true, null
            );

        } catch (Exception e) {
            return new StatusResponse(
                    false,
                    e.getMessage()
            );
        }
    }

    @Override
    public User confirmRegistration(String hash) throws LinkExpiredException, Base64OperationException {
        var signUpRequest = base64.decode(hash, SignUpRequest.class);
        long currentTimeInSeconds = System.currentTimeMillis() / 1000;
        // Если пользователь перешел по ссылке спустя 24 часа, она не валидна
        if (currentTimeInSeconds - signUpRequest.getTimeOfRequest() > 8460) {
            throw new LinkExpiredException("Link expired");
        }
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setWork(signUpRequest.getWork());
        user.setDateOfBirth(signUpRequest.getDateOfBirth());
        user.setRole(Role.USER);
        user.setCreatedAt(System.currentTimeMillis()/1000);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return userRepository.save(user);
    }


    @Override
    public User signup(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setWork(signUpRequest.getWork());
        user.setDateOfBirth(signUpRequest.getDateOfBirth());
        user.setRole(Role.USER);
        user.setCreatedAt(System.currentTimeMillis()/1000);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(),
                signInRequest.getPassword()));

        var user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String username = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByUsername(username).orElseThrow();
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(user);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }
}
