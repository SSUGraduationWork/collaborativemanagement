package com.example.upload.Service;

import com.example.upload.Repository.BoardRepository;
import com.example.upload.Repository.FeedbackRepository;

import com.example.upload.Repository.FeedbackStatusRepository;
import com.example.upload.Repository.MemberRepository;
import com.example.upload.domain.Boards;


import com.example.upload.domain.FeedbackStatuses;
import com.example.upload.domain.Feedbacks;
import com.example.upload.domain.Members;
import com.example.upload.dto.request.BoardWriteRequest;
import com.example.upload.dto.request.FeedbackRequest;
import com.example.upload.dto.response.BoardResponse;
import com.example.upload.dto.response.FeedbackResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
public class FeedbackService {


    private  FeedbackRepository feedbackRepository;
    private  BoardRepository boardRepository;

    private MemberRepository memberRepository;
    private FeedbackStatusRepository feedbackStatusRepository;


    public FeedbackResponse save(Long boardId, Long memberId, FeedbackRequest requset, boolean isApproved) {

        Boards boards =boardRepository.findById(boardId).get();
        Members writers =memberRepository.findById(memberId).get();
        Feedbacks feedbacks= toEntity(requset);
        feedbacks.confirmBoard(boards);
        feedbacks.confirmMember(writers);


        //boards, writers로 feedbackStatus찾기
        FeedbackStatuses feedbackStatus =feedbackStatusRepository.findByBoardsAndUsers(boards, writers);
        if (isApproved) {
            // 승인한 경우 feedbackYn=true로 바꾸기
            feedbackStatus.feedbackAgree();
        } else {
            // 거부한 경우 feedbackYn=false로 바꾸기
            feedbackStatus.feedbackDeny();
        }
        feedbackRepository.save(feedbacks);



        // 팀원 모두 동의하면 board의 feedback_yn=true로 변경
        // Board에 해당하는 모든 FeedbackStatuses 조회
        List<FeedbackStatuses> feedbackStatusesList = feedbackStatusRepository.findByBoards(boards);

        // FeedbackStatuses가 존재하는 경우에만 처리
        if (!feedbackStatusesList.isEmpty()) {
            boolean hasFeedbackYnTrue = true;

            // 모든 FeedbackStatuses의 feedback_yn이 true인지 확인
            for (FeedbackStatuses feedbackStatuses : feedbackStatusesList) {
                if (!feedbackStatuses.isFeedbackYn()) { //한명이라도 feedback을 안했으면
                    hasFeedbackYnTrue = false;
                    break;
                }
            }

            // 모든 FeedbackStatuses의 feedback_yn이 true라면 board의 feedback_yn도 true로 변경
            if (hasFeedbackYnTrue) {
                boards.setFeedbackYn(true);
                boardRepository.save(boards);
            }
        }

        return FeedbackResponse.from(feedbacks,boards);
    }




    public static Feedbacks toEntity(FeedbackRequest feedbackRequest) {
        return Feedbacks.builder()
                .comment(feedbackRequest.getComment())
                .build();
    }



    //피드백 불러오기

    public List<FeedbackResponse> feedbackView(Long boardId) {
        List<Feedbacks> feedbackList = feedbackRepository.findAllByBoardsId(boardId);

        return feedbackList.stream()
                .map(feedback -> FeedbackResponse
                        .builder()
                        .comment(feedback.getComment())
                        .boardId(feedback.getBoards().getId())
                        .feedbackId(feedback.getId())
                        .build())
                .collect(Collectors.toList());
    }

}
