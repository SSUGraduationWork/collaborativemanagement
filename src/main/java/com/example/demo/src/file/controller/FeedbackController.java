package com.example.demo.src.file.controller;

import com.example.demo.src.file.Service.FeedbackService;
import com.example.demo.src.file.common.CommonCode;
import com.example.demo.src.file.common.Response;
import com.example.demo.src.file.dto.request.FeedbackRequest;
import com.example.demo.src.file.dto.response.BoardFeedbackResponse;
import com.example.demo.src.file.dto.response.FeedbackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="http://localhost:8081")
public class FeedbackController {

    private final FeedbackService feedbackService;

    //피드백 글쓰기
    //isApproved=0인경우 피드백 거부, isApproved=1인경우 피드백 승인
    @PostMapping("/comment/{boardId}/{writerId}/{isApproved}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Response<FeedbackResponse>>  commentSave(@PathVariable("boardId") Long boardId,
                                                                   @PathVariable("writerId") Long writerId,
                                                                   FeedbackRequest request,
                                                                   @PathVariable("isApproved") boolean isApproved){
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, feedbackService.save(boardId,writerId,request,isApproved)));
    }

    //피드백 반영하여 수정한 게시판에 대한 재수락, 재수정
    @PostMapping("/recomment/{boardId}/{writerId}/{isApproved}")
    @ResponseStatus(HttpStatus.CREATED)
    public void  reApproved(@PathVariable("boardId") Long boardId,
                            @PathVariable("writerId") Long writerId,
                            @PathVariable("isApproved") boolean isApproved){
        feedbackService.reFeedback(boardId,writerId,isApproved);
    }


    //피드백 보기
    @GetMapping("/comment/{boardId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Response<List<BoardFeedbackResponse>>>  commentView(@PathVariable("boardId") Long boardId){
       return  ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, feedbackService.feedbackView(boardId)));
    }



}
