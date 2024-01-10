package com.julant7.authservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name",nullable = false)
    @Size(max = 50)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    @Size(max = 50)
    private String lastName;

    @Column(name = "username",nullable = false, unique = true)
    @Size(max = 50)
    private String username;

    @Column(name = "email",nullable = false, unique = true)
    @Size(max = 62)
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "password",nullable = false)
    @Size(min = 8)
    private String password;

    @Column(name = "role",nullable = false)
    private Role role = Role.USER;

    @Column(name = "date_of_birth",nullable = false)
    private Long dateOfBirth;

    @Column(name = "created_at",nullable = false)
    private Long createdAt;

    @Column(name = "bio")
    private String bio;

    @Column(name = "location")
    private String location;

    @Column(name = "work")
    private String work;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;

    public User() {
    }

    public User(Long id, String firstName, String lastName, String username, String email, String password, Role role, Long dateOfBirth, Long createdAt, String bio, String location, String work, boolean accountNonLocked) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = createdAt;
        this.bio = bio;
        this.location = location;
        this.work = work;
        this.accountNonLocked = accountNonLocked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public String getFullName() { return firstName + lastName; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return accountNonLocked == user.accountNonLocked && Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && role == user.role && Objects.equals(dateOfBirth, user.dateOfBirth) && Objects.equals(createdAt, user.createdAt) && Objects.equals(bio, user.bio) && Objects.equals(location, user.location) && Objects.equals(work, user.work);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, username, email, password, role, dateOfBirth, createdAt, bio, location, work, accountNonLocked);
    }
}
