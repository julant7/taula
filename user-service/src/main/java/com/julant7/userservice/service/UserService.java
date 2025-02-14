package com.julant7.userservice.service;

import com.julant7.userservice.dto.*;
import com.julant7.userservice.exception.JwtException;
import com.julant7.userservice.exception.SendingMailException;
import com.julant7.userservice.exception.UserException;
import com.julant7.userservice.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    String confirmRegistration(SignUpRequest signUpRequest) throws UserException, SendingMailException;

    JwtAuthenticationResponse register(RegisterRequest registerRequest) throws JwtException;

    User signup(SignUpRequest signUpRequest);

    User getInfo(String token) throws UserException, JwtException;

    User findUserById(Long id) throws UserException;
    User updateUser(Long userId, UpdateUserRequest req) throws UserException;

    List<User> searchUser(String query);

    User findUserByJwt(String jwt) throws UserException, JwtException;

    void resetPassword(ResetPasswordRequest resetPasswordRequest) throws UserException, SendingMailException;

    void saveNewPassword(SaveNewPasswordRequest saveNewPasswordRequest) throws UserException, JwtException;

    String updatePassword(String token, ChangePasswordRequest changePasswordRequest) throws JwtException, UserException;

    void confirmUpdateEmail(String token, ConfirmUpdateEmailRequest confirmUpdateEmailRequest) throws JwtException, SendingMailException;

    void updateEmail(UpdateEmailRequest updateEmailRequest) throws JwtException;

    String setUser2FA(String token, SetUser2FARequest setUser2FARequest) throws JwtException;

    IsUsing2FAResponse isUsing2FA(String email) throws UserException;
}
