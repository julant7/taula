package com.julant7.userservice.dto;

import lombok.Getter;

@Getter
public class SaveNewPasswordRequest {
    private String token;
    private String newPassword;
}
