package com.example.upload.controller;

import com.example.upload.Service.FeedbackService;

import com.example.upload.common.CommonCode;
import com.example.upload.common.Response;
import com.example.upload.domain.Feedbacks;
import com.example.upload.dto.request.FeedbackRequest;
import com.example.upload.dto.response.BoardResponse;
import com.example.upload.dto.response.FeedbackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    //피드백 글쓰기
    @PostMapping("/comment/{boardId}/{writerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Response<FeedbackResponse>>  commentSave(@PathVariable("boardId") Long boardId,
                                                                   @PathVariable("writerId") Long writerId,
                                                                   FeedbackRequest request){
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, feedbackService.save(boardId,writerId,request)));
    }

    //피드백 보기
    @GetMapping("/comment/{boardId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Response<List<FeedbackResponse>>>  commentView(@PathVariable("boardId") Long boardId){
       return  ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, feedbackService.feedbackView(boardId)));
    }



}
