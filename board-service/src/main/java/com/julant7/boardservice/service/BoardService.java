package com.julant7.boardservice.service;

import com.julant7.boardservice.dto.CreateBoardRequest;
import com.julant7.boardservice.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

public interface BoardService {

    Board createBoard(@RequestHeader("Authorization") String authorizationHeader, CreateBoardRequest createBoardRequest);
}