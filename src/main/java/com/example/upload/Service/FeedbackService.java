package com.example.upload.Service;

import com.example.upload.Repository.BoardRepository;
import com.example.upload.Repository.FeedbackRepository;
import com.example.upload.domain.Board;
import com.example.upload.domain.Feedback;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@AllArgsConstructor
public class FeedbackService {


    private  FeedbackRepository feedbackRepository;
    private  BoardRepository boardRepository;



    public void save(Long boardId, Feedback feedback) {
        Board board =boardRepository.findById(boardId).get();
        feedback.confirmBoard(board);

        feedbackRepository.save(feedback);
    }


    //피드백 불러오기
    public List<String>  feedbackView(Long boardId) {
        List<Feedback> feedbackList = feedbackRepository.findAllByBoardId(boardId);
        List<String> comments = new ArrayList<>();

        for (Feedback feedback : feedbackList) {
            comments.add(feedback.getComment());
        }

        return comments;

    }




}
