package com.julant7.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Verify2FACodeResponse {
    private boolean isCorrect;
}
