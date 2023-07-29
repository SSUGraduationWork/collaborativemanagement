package com.example.upload.controller;

import com.example.upload.Service.FeedbackService;
import com.example.upload.domain.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    //피드백 글쓰기
    @PostMapping("/comment/{boardId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void commentSave(@PathVariable("boardId") Long boardId, Feedback feedback){
        feedbackService.save(boardId, feedback);
    }

    //피드백 보기
    @GetMapping("/comment/{boardId}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<String> commentView(@PathVariable("boardId") Long boardId){
       return  feedbackService.feedbackView(boardId);
    }



}
