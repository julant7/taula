package com.julant7.userservice.repository;

import com.julant7.userservice.model.Jwt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//@EnableRedisRepositories
public interface JwtRepository extends CrudRepository<Jwt, Long> {

    Optional<Jwt> findByUserId(Long userId);
}
