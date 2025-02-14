package com.julant7.userservice.dto;


import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UpdateUserRequest {
    private String firstName;
    private String lastName;

    @Pattern(regexp = "/^[^._ ](?:[\\w-]|\\.[\\w-])+[^._ ]$", message = "Email should be valid")
    private String username;
    private String email;
    private String bio;
    private String location;
    private String work;
}
