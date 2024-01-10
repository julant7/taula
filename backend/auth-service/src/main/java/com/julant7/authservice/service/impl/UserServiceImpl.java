package com.julant7.authservice.service.impl;

import com.julant7.authservice.dto.UpdateUserRequest;
import com.julant7.authservice.exception.UserException;
import com.julant7.authservice.model.User;
import com.julant7.authservice.repository.UserRepository;
import com.julant7.authservice.service.JWTService;
import com.julant7.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JWTService jwtService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JWTService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public User getInfo(String token) throws UserException {
        var userName = jwtService.extractUserName(token);
        Optional<User> userOptional = userRepository.findByEmail(userName);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new UserException("JWT Token is invalid");
    }

    @Override
    public User findUserById(Long id) throws UserException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new UserException("User not found with id " + id);
    }

    @Override
    public User updateUser(Long userId, UpdateUserRequest req) throws UserException {
        User user = findUserById(userId);
        if (req.getFirstName() != null) {
            user.setFirstName(req.getFirstName());
        }
        if (req.getLastName() != null) {
            user.setLastName(req.getLastName());
        }
        if (req.getUsername() != null) {
            user.setUsername(req.getUsername());
        }
        if (req.getEmail() != null) {
            user.setEmail(req.getEmail());
        }
        if (req.getDateOfBirth() != null) {
            user.setDateOfBirth(req.getDateOfBirth());
        }
        if (req.getBio() != null) {
            user.setBio(req.getBio());
        }
        if (req.getLocation() != null) {
            user.setLocation(req.getLocation());
        }
        if (req.getWork() != null) {
            user.setWork(req.getWork());
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String query) {
        return userRepository.searchUser(query);
    }


    @Override
    public User findUserByJwt(String jwt) throws UserException {
        var email = jwtService.extractUserName(jwt);
        if (email == null) {
            throw new BadCredentialsException("Invalid Jwt Token");
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new UserException("User with this email not found");
    }
}
