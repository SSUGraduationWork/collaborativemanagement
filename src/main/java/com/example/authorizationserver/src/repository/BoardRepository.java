package com.example.authorizationserver.src.repository;

import com.example.authorizationserver.src.authorization.dto.MemberDto;
import com.example.authorizationserver.src.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByUserIdAndBoardId(Long userId, Long boardId);
}
