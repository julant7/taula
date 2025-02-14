package com.julant7.userservice.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ChangePasswordRequest {
    private String lastPassword;

    @Pattern(regexp = "^[a-zA-Z0-9\\\\s\\\\p{P}]+$", message = "Password can only consist of the Latin alphabet, numbers and special characters")
    @Size(min = 8, message = "The password must consist of at least 8 characters")
    private String newPassword;

    private String code;
}
