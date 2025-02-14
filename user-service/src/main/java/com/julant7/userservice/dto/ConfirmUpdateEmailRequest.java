package com.julant7.userservice.dto;

import lombok.Getter;

@Getter
public class ConfirmUpdateEmailRequest {
    private String newEmail;
    private String code;
}
