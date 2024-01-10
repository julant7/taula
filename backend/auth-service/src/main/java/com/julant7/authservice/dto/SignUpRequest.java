package com.julant7.authservice.dto;

public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Long dateOfBirth;
    private String work;
    private String password;
    private final Long timeOfRequest = System.currentTimeMillis() / 1000;

    public SignUpRequest() {

    }
    public SignUpRequest(String firstName, String lastName, String email, String username, Long dateOfBirth, String work, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.work = work;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getUsername() {
        return username;
    }
    public Long getDateOfBirth() {
        return dateOfBirth;
    }
    public String getWork() {
        return work;
    }
    public String getPassword() {
        return password;
    }
    public Long getTimeOfRequest() { return timeOfRequest; }
}
