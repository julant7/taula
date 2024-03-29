package com.julant7.authservice.repository;

import com.julant7.authservice.model.Role;
import com.julant7.authservice.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    User findByRole(Role role);

    @Query("select u from User u where u.firstName like %:query%")
    List<User> searchUser(@Param("query") String query);
}
