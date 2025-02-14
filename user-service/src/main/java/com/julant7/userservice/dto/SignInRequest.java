package com.julant7.userservice.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SignInRequest {
    // This field may be username or email
    @NonNull
    String credential;
    @NonNull
    String password;
}