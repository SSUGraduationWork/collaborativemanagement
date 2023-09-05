package com.example.demo.src.file.Repository;

import com.example.demo.src.file.domain.Boards;
import com.example.demo.src.file.domain.FeedbackStatuses;
import com.example.demo.src.file.domain.Members;
import com.example.demo.src.file.domain.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackStatusRepository extends JpaRepository<FeedbackStatuses, Long> {



    FeedbackStatuses findByBoardsAndUsers(Boards boards, Members users);

    List<FeedbackStatuses> findAllByBoardsId(Long boardId);

    List<FeedbackStatuses> findByBoards(Boards boards);

    List<FeedbackStatuses>  findByTeams(Teams teams);

    List<FeedbackStatuses> findByUsersIdAndTeamsId(Long memberId, Long teamId);
    FeedbackStatuses findByBoardsIdAndUsersId(Long boardId, Long memberId);
    List<FeedbackStatuses> findByUsers(Members member);
}
