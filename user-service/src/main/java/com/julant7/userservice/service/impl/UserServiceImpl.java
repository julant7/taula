package com.julant7.userservice.service.impl;

import com.julant7.userservice.dto.*;
import com.julant7.userservice.exception.JwtException;
import com.julant7.userservice.exception.SendingMailException;
import com.julant7.userservice.exception.UserException;
import com.julant7.userservice.model.Role;
import com.julant7.userservice.model.User;
import com.julant7.userservice.repository.UserRepository;
import com.julant7.userservice.service.AuthenticationService;
import com.julant7.userservice.service.JwtService;
import com.julant7.userservice.service.KafkaProducer;
import com.julant7.userservice.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final KafkaProducer kafkaProducer;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public static final String EMAIL_VERIFICATION = "email_verification";
    public static final String EMAIL_UPDATE = "update_email";
    private final String jwtErrorMessage = "Invalid JWT Token";

    @Override
    public String confirmRegistration(SignUpRequest signUpRequest) throws UserException, SendingMailException {
        Optional<User> user = userRepository.findByEmail(signUpRequest.getEmail());
        if (user.isPresent()) {
            throw new UserException("Email: %s registered yet.".formatted(signUpRequest.getEmail()));
        }
        String dataToSend = jwtService.generateTokenForRegistration(signUpRequest);
        try {
            kafkaProducer.produce(EMAIL_VERIFICATION, new KafkaEmailMessageRequest(signUpRequest.getEmail(), dataToSend));
            return "Email was successfully sent";
        } catch (Exception e) {
            throw new SendingMailException("Error while sending email");
        }
    }

    @Override
    public JwtAuthenticationResponse register(RegisterRequest registerRequest) throws JwtException {
        String token = registerRequest.getToken();
        if (jwtService.isTokenValid(token)) {
            var jwtClaims = jwtService.extractALlClaims(token);
            // If the user clicked on the link after 24 hours, it's not valid
//            if (jwtClaims.getExpiration().before(new Date())) throw new LinkExpiredException("Link expired");
            User user = new User();
            String username = (String) jwtClaims.get("username");
            String password = (String) jwtClaims.get("password");
            user.setUsername(username);
            user.setEmail((String) jwtClaims.get("email"));
            user.setFirstName((String) jwtClaims.get("firstName"));
            user.setLastName((String) jwtClaims.get("lastName"));
            user.setWork((String) jwtClaims.get("work"));
            user.setRole(Role.USER);
            user.setCreatedAt(System.currentTimeMillis() / 1000);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String refreshToken = jwtService.generateRefreshToken(user);
            String accessToken = jwtService.generateAccessToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setRefreshToken(refreshToken);
            jwtAuthenticationResponse.setAccessToken(accessToken);
            return jwtAuthenticationResponse;
        }
        throw new JwtException(jwtErrorMessage);
    }

    @Override
    public User signup(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setWork(signUpRequest.getWork());
        user.setRole(Role.USER);
        user.setCreatedAt(System.currentTimeMillis()/1000);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User getInfo(String token) throws JwtException {
        var userId = jwtService.extractUserId(token);
        if (userId == null)
            throw new JwtException("JWT Token is invalid");
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new JwtException("JWT Token is invalid");
    }

    @Override
    public User findUserById(Long id) throws UserException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) return userOptional.get();
        throw new UserException("User not found with id " + id);
    }

    @Override
    public User updateUser(Long userId, UpdateUserRequest req) throws UserException {
        User user = findUserById(userId);
        if (req.getFirstName() != null) user.setFirstName(req.getFirstName());
        if (req.getLastName() != null) user.setLastName(req.getLastName());
        if (req.getUsername() != null) user.setUsername(req.getUsername());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        if (req.getBio() != null) user.setBio(req.getBio());
        if (req.getLocation() != null) user.setLocation(req.getLocation());
        if (req.getWork() != null) user.setWork(req.getWork());
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String query) {
        return userRepository.searchUser(query);
    }


    @Override
    public User findUserByJwt(String jwt) throws UserException, JwtException {
        var userId = jwtService.extractUserId(jwt);
        if (userId == null) throw new JwtException("Invalid Jwt Token");
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new UserException("User with this email not found");
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) throws UserException, SendingMailException {
        String credential = resetPasswordRequest.getCredential();
        User user;
        String email;
        if (credential.contains("@")) {
            user = userRepository.findByEmail(credential).orElseThrow(() -> new UserException("User with this email doesn't exist."));
            email = credential;
        } else {
            user = userRepository.findByUsername(credential).orElseThrow(() -> new UserException("User with this username doesn't exist."));
            email = user.getEmail();
        }
        if (user.isUsing2FA() && !authenticationService.isValidVerificationCode(user, resetPasswordRequest.getCode())) {
            throw new BadCredentialsException("Invalid verification Code");
        }
        String dataToSend = jwtService.generateTokenForResetPassword(user.getId());
        try {
            kafkaProducer.produce(EMAIL_VERIFICATION, new KafkaEmailMessageRequest(email, dataToSend));
        } catch (Exception e) {
            throw new SendingMailException("Error while sending email");
        }
    }

    @Override
    public void saveNewPassword(SaveNewPasswordRequest saveNewPasswordRequest) throws UserException, JwtException {
        String token = saveNewPasswordRequest.getToken();
        String newPassword = saveNewPasswordRequest.getNewPassword();
        if (jwtService.isTokenValid(token)) {
            Long userId = Long.valueOf(jwtService.extractUserId(token));
            User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found."));
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
        throw new JwtException("Invalid Token");
    }

    @Override
    public String updatePassword(String token, ChangePasswordRequest changePasswordRequest) throws JwtException, UserException {
        if (jwtService.isTokenValid(token)) {
            Long userId = Long.valueOf(jwtService.extractUserId(token));
            User user = userRepository.findById(userId).orElseThrow(() -> new JwtException("Invalid JWT Token"));
            if (user.isUsing2FA() && !authenticationService.isValidVerificationCode(user, changePasswordRequest.getCode())) {
                throw new BadCredentialsException("Invalid verification Code");
            }
            if (!Objects.equals(user.getPassword(), passwordEncoder.encode(changePasswordRequest.getLastPassword()))) {
                throw new UserException("Invalid credentials");
            }
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
            return "Password has been successfully changed";
        }
        throw new JwtException("Invalid JWT Token");
    }
    @Override
    public void confirmUpdateEmail(String token, ConfirmUpdateEmailRequest confirmUpdateEmailRequest) throws JwtException, SendingMailException {
        String newEmail = confirmUpdateEmailRequest.getNewEmail();
        if (jwtService.isTokenValid(token)) {
            Long userId = Long.valueOf(jwtService.extractUserId(token));
            User user = userRepository.findById(userId).orElseThrow(() -> new JwtException("Invalid JWT Token"));
            if (user.isUsing2FA() && !authenticationService.isValidVerificationCode(user, confirmUpdateEmailRequest.getCode())) {
                throw new BadCredentialsException("Invalid verification Code");
            }
            String dataToSend = jwtService.generateTokenForUpdateEmail(userId, newEmail);
            try {
                kafkaProducer.produce(EMAIL_UPDATE, new KafkaEmailMessageRequest(newEmail, dataToSend));
            } catch (Exception e) {
                throw new SendingMailException("Error while sending email");
            }
        }
        throw new JwtException("Invalid JWT Token");
    }

    @Override
    public void updateEmail(UpdateEmailRequest updateEmailRequest) throws JwtException {
        String token = updateEmailRequest.getToken();
        if (jwtService.isTokenValid(token)) {
            Claims jwtClaims = jwtService.extractALlClaims(token);
            Object userId = jwtClaims.get("userId");
            Object newEmail = jwtClaims.get("newEmail");
            if (userId == null || newEmail == null) throw new JwtException("Invalid JWT Token");
            User user = userRepository.findById((Long) userId).orElseThrow(() -> new JwtException("Invalid JWT Token"));
            user.setEmail((String) newEmail);
            userRepository.save(user);
        }
        throw new JwtException("Invalid JWT Token");
    }

    @Override
    public String setUser2FA(String token, SetUser2FARequest setUser2FARequest) throws JwtException {
        if (jwtService.isTokenValid(token)) {
            String userId = jwtService.extractUserId(token);
            if (userId == null) throw new JwtException(jwtErrorMessage);
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new JwtException(jwtErrorMessage));
            if (user.isUsing2FA() && !authenticationService.isValidVerificationCode(user, setUser2FARequest.getCode())) {
                throw new BadCredentialsException("Invalid verification Code");
            }
            user.setUsing2FA(setUser2FARequest.isUse2FA());
        }
        throw new JwtException(jwtErrorMessage);
    }
    @Override
    public IsUsing2FAResponse isUsing2FA(String credential) throws UserException {
        User user;
        if (credential.contains("@")) {
            user = userRepository.findByEmail(credential).orElseThrow(() -> new UserException("User with this email doesn't exist."));
        } else {
            user = userRepository.findByUsername(credential).orElseThrow(() -> new UserException("User with this username doesn't exist."));
        }
        IsUsing2FAResponse isUsing2FAResponse = new IsUsing2FAResponse();
        isUsing2FAResponse.setUsing2FA(user.isUsing2FA());
        return isUsing2FAResponse;
    }

}
