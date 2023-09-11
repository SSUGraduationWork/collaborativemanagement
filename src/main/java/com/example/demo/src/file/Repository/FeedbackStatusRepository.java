package com.example.demo.src.file.Repository;

import com.example.demo.src.file.domain.Boards;
import com.example.demo.src.file.domain.FeedbackStatuses;
import com.example.demo.src.file.domain.Members;
import com.example.demo.src.file.domain.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FeedbackStatusRepository extends JpaRepository<FeedbackStatuses, Long> {




    @Query("SELECT fs FROM FeedbackStatuses fs WHERE fs.boards = :boards AND fs.users = :users")
    FeedbackStatuses findByBoardsAndUsers(@Param("boards") Boards boards, @Param("users") Members users);

    @Query("SELECT fs FROM FeedbackStatuses fs WHERE fs.boards.id = :boardId")
    List<FeedbackStatuses> findAllByBoardsId(@Param("boardId") Long boardId);

    @Query("SELECT fs FROM FeedbackStatuses fs WHERE fs.boards.id = :boardId AND fs.users.id = :memberId")
    FeedbackStatuses findByBoardsIdAndUsersId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    @Query("SELECT fs FROM FeedbackStatuses fs WHERE fs.users.id = :memberId AND fs.teams.id = :teamId")
    List<FeedbackStatuses> findFeedbackStatusesByMemberIdAndTeamId(@Param("memberId") Long memberId, @Param("teamId") Long teamId);
    // FeedbackStatuses를 일괄 저장하는 쿼리

    @Query("SELECT fs FROM FeedbackStatuses fs WHERE fs.boards = :boards")
    List<FeedbackStatuses> findByBoards(@Param("boards") Boards boards);


}
