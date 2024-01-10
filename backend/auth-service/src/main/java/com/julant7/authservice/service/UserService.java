package com.julant7.authservice.service;

import com.julant7.authservice.dto.UpdateUserRequest;
import com.julant7.authservice.exception.UserException;
import com.julant7.authservice.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();

    User getInfo(String token) throws UserException;

    User findUserById(Long id) throws UserException;
    User updateUser(Long userId, UpdateUserRequest req) throws UserException;

    List<User> searchUser(String query);

    User findUserByJwt(String jwt) throws UserException;

}
