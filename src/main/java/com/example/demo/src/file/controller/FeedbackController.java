package com.example.demo.src.file.controller;

import com.example.demo.src.file.Service.FeedbackService;
import com.example.demo.src.file.common.CommonCode;
import com.example.demo.src.file.common.FeedbackYnResponse;
import com.example.demo.src.file.common.Response;
import com.example.demo.src.file.domain.Members;
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
//@CrossOrigin(origins="http://localhost:8081")
public class FeedbackController {

    private final FeedbackService feedbackService;

    //피드백 글쓰기
    //isApproved=0인경우 피드백 거부, isApproved=1인경우 피드백 승인
    @PostMapping("/comment/{boardId}/{writerId}/{isApproved}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Response<FeedbackResponse>>  commentSave(@PathVariable("boardId") Long boardId,
                                                                   @PathVariable("writerId") Long writerId,
                                                                   FeedbackRequest request,
                                                                   @PathVariable("isApproved") Integer isApproved){
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, feedbackService.save(boardId,writerId,request,isApproved)));
    }

    //피드백 반영하여 수정한 게시판에 대한 재수락, 재수정
    @PostMapping("/recomment/{boardId}/{writerId}/{isApproved}")
    @ResponseStatus(HttpStatus.CREATED)
    public void  reApproved(@PathVariable("boardId") Long boardId,
                            @PathVariable("writerId") Long writerId,
                            @PathVariable("isApproved") Integer isApproved){
        feedbackService.reFeedback(boardId,writerId,isApproved);
    }


    //피드백 보기
    //front에서 피드백을 쓰자마자 바로 보여주기 위해서 addComment라는 변수에 담아서 화면에 보여줬음.
    //feedback의 거절 승인은 한번하여 번복이 없음. feedback 승인,여부가 필요함
    @GetMapping("/comment/{boardId}/{memberId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FeedbackYnResponse<List<BoardFeedbackResponse>>>  commentFeedbackView(@PathVariable("boardId") Long boardId,
                                                                                                @PathVariable("memberId") Long memberId){

        Members members=feedbackService.getUsers(memberId);
        return  ResponseEntity.ok(FeedbackYnResponse.of(CommonCode.GOOD_REQUEST, feedbackService.feedbackView(boardId),feedbackService.getFeedbackYn(boardId,memberId),members.getPictureUrl(),members.getUserName(),members.getStudentNumber()));
    }



}
