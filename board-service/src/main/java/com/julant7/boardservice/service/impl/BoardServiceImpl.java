package com.julant7.boardservice.service.impl;

import com.julant7.boardservice.dto.CreateBoardRequest;
import com.julant7.boardservice.model.Board;
import com.julant7.boardservice.repository.BoardRepository;
import com.julant7.boardservice.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final BoardService boardService;

    public BoardServiceImpl(BoardRepository boardRepository, BoardService boardService) {
        this.boardRepository = boardRepository;
        this.boardService = boardService;
    }

    @Override
    public Board createBoard(String token, CreateBoardRequest createBoardRequest) {
//        String username = jwtService.extractUserName(refreshTokenRequest.getToken());
//        User user = userRepository.findByUsername(username).orElseThrow();
//        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
//            Set<User> admins = new HashSet<>() {{
//                add(user);
//            }};
//
//            Board board = new Board();
//            board.setAdmins(Set<user>);
//            board.setName(createBoardRequest.getName());
//            board.setDescription(createBoardRequest.getDescription());
//            return boardRepository.save(board);
//        }
        return null;
    }
}
