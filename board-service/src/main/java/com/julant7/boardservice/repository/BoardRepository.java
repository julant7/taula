package com.julant7.boardservice.repository;

import com.julant7.boardservice.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findById(Long aLong);
}
