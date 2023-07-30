package com.example.upload.Service;

import com.example.upload.Repository.BoardRepository;
import com.example.upload.Repository.FeedbackRepository;

import com.example.upload.Repository.MemberRepository;
import com.example.upload.domain.Boards;


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


    public FeedbackResponse save(Long boardId, Long memberId, FeedbackRequest requset) {

        Boards boards =boardRepository.findById(boardId).get();
        Members writers =memberRepository.findById(memberId).get();
        Feedbacks feedbacks= toEntity(requset);
        feedbacks.confirmBoard(boards);
        feedbacks.confirmMember(writers);
        feedbackRepository.save(feedbacks);
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
