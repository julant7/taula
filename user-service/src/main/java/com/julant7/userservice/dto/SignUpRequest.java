package com.julant7.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class SignUpRequest {
    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    @Pattern(regexp = "^(?=.{5,50}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$", message = "Username should be valid")
    private String username;

    @NonNull
    @Email(regexp = ".+@.+", message = "Email should be valid")
    private String email;

    @NonNull
    private String work;

    @NonNull
    @Pattern(regexp = "^[a-zA-Z0-9\\\\s\\\\p{P}]+$", message = "Password can only consist of the Latin alphabet, numbers and special characters")
    @Size(min = 8, message = "The password must consist of at least 8 characters")
    private String password;

    private final Long timeOfRequest = System.currentTimeMillis() / 1000;
}
