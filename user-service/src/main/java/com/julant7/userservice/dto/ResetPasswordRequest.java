package com.julant7.userservice.dto;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {
    private String credential;
    private String code;
}
