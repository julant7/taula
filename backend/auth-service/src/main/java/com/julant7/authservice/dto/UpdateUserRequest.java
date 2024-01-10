package com.julant7.authservice.dto;


public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Long dateOfBirth;
    private String bio;
    private String location;
    private String work;

    public UpdateUserRequest() {

    }

    public UpdateUserRequest(String firstName, String lastName, String username, String email, Long dateOfBirth, String bio, String location, String work) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
        this.location = location;
        this.work = work;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    public String getBio() {
        return bio;
    }

    public String getLocation() {
        return location;
    }

    public String getWork() {
        return work;
    }
}
