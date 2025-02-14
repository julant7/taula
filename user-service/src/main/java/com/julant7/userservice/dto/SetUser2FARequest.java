package com.julant7.userservice.dto;

import lombok.Getter;

@Getter
public class SetUser2FARequest {
    private String code;
    private boolean use2FA;
}
