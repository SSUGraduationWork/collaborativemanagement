package com.example.demo.src.file.Service;

import com.example.demo.src.file.Repository.BoardRepository;
import com.example.demo.src.file.Repository.FeedbackStatusRepository;
import com.example.demo.src.file.Repository.MembersRepository;
import com.example.demo.src.file.Repository.TeamRepository;
import com.example.demo.src.file.domain.Boards;
import com.example.demo.src.file.domain.FeedbackStatuses;
import com.example.demo.src.file.domain.Members;
import com.example.demo.src.file.domain.Teams;
import com.example.demo.src.file.dto.response.BoardResponse;
import com.example.demo.src.file.dto.response.FeedbackStatusResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FeedbackStatusService {

    private FeedbackStatusRepository feedbackStatusRepository;
    private MembersRepository membersRepository;
    private TeamRepository teamRepository;
    private BoardRepository boardRepository;

    public FeedbackStatusResponse countFeedbacksForMemberAndTeam(Long memberId, Long teamId) {
        // memberId를 사용하여 해당 멤버의 알람 리스트를 가져옴
        Members member = membersRepository.findById(memberId).get();
        Teams teams = teamRepository.findById(teamId).get();

        List<Boards> boardsList = boardRepository.findByTeams(teams);
        List<BoardResponse> boardResponses = new ArrayList<>();
        Integer finishedCnt = 0; // 피드백을 한 경우 카운트
        Integer todoCnt = 0; // 피드백을 안 한 경우 카운트

        for (Boards boards : boardsList) {
            FeedbackStatuses feedbackStatus = feedbackStatusRepository.findByBoardsAndUsers(boards, member);

            BoardResponse boardResponse = BoardResponse.from(boards);

            if (boards.getUsers().getId().equals(memberId)) {
                boardResponse.setFeedbackYn(true);
            } else {
                boardResponse.setFeedbackYn(feedbackStatus.isFeedbackYn());
                if (feedbackStatus.isFeedbackYn()) {
                    finishedCnt++;
                } else {
                    todoCnt++;
                }
            }

        }
        return FeedbackStatusResponse.builder()
                .finishedCnt(finishedCnt)
                .todoCnt(todoCnt)
                .build();
    }
}








