package com.julant7.userservice.controllers;

import com.julant7.userservice.dto.*;
import com.julant7.userservice.exception.JwtException;
import com.julant7.userservice.exception.SendingMailException;
import com.julant7.userservice.exception.UserException;
import com.julant7.userservice.model.User;
import com.julant7.userservice.service.UserService;
import jakarta.validation.*;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    private final UserService userService;

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody @Valid SignUpRequest signUpRequest) {
//        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(signUpRequest);
//        List<String> errors = new ArrayList<>();
//        if (!violations.isEmpty()) {
//            for (ConstraintViolation<SignUpRequest> violation : violations) {
//                errors.add(violation.getMessage());
//            }
//            return new CustomConstraintViolationException(errors);
//        }
//        else {
        return ResponseEntity.ok(userService.signup(signUpRequest));
//        }
    }

    @PostMapping("/confirm_registration")
    public ResponseEntity<String> confirmRegistration(@RequestBody SignUpRequest signUpRequest) throws SendingMailException, UserException {
        return ResponseEntity.ok(userService.confirmRegistration(signUpRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) throws JwtException {
        return ResponseEntity.ok(userService.register(registerRequest));
    }

    @GetMapping("/is_using_2fa/{credential}")
    public ResponseEntity<IsUsing2FAResponse> isUsing2FA(@PathVariable String credential) throws JwtException, UserException {
        return ResponseEntity.ok(userService.isUsing2FA(credential));
    }

    @PostMapping("/reset_password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) throws SendingMailException, UserException {
        userService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save_new_password")
    public ResponseEntity<Void> saveNewPassword(@RequestBody SaveNewPasswordRequest saveNewPasswordRequest) throws UserException, JwtException {
        userService.saveNewPassword(saveNewPasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update_password")
    public ResponseEntity<Void> updatePassword(@RequestHeader("Authorization") String authorizationHeader,
                                               @RequestBody ChangePasswordRequest changePasswordRequest)
            throws UserException, JwtException {
        String token = authorizationHeader.substring(7);
        userService.updatePassword(token, changePasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm_update_email")
    public ResponseEntity<Void> confirmUpdateEmail(@RequestHeader("Authorization") String authorizationHeader,
                                                   @RequestBody ConfirmUpdateEmailRequest confirmUpdateEmailRequest)
            throws SendingMailException, JwtException {
        String token = authorizationHeader.substring(7);
        userService.confirmUpdateEmail(token, confirmUpdateEmailRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update_email")
    public ResponseEntity<Void> updateEmail(@RequestBody UpdateEmailRequest updateEmailRequest) throws JwtException {
        userService.updateEmail(updateEmailRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/set_2fa")
    public ResponseEntity<String> setUser2FA(@RequestHeader("Authorization") String authorizationHeader, @RequestBody SetUser2FARequest setUser2FARequest) throws JwtException {
        String token = authorizationHeader.substring(7);
        return ResponseEntity.ok(userService.setUser2FA(token, setUser2FARequest));
    }
}
