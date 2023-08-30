package com.example.demo.src.file.controller;

import com.example.demo.src.file.Service.FeedbackStatusService;
import com.example.demo.src.file.common.CommonCode;
import com.example.demo.src.file.common.Response;
import com.example.demo.src.file.dto.response.FeedbackStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


//____________________________________________
//문제점: feedbackStatus를 member와 team으로 조회하려고 했는데 연관관계 에러가 남
//원인: board가 member와 feedbackStatus와 일대 다로 연결되어 있는데 feedbackStatus가 member와 일대 다로 역결되어 있어
//무한 쿼리가 나가는 것 같음
//아직 해결을 못했지만 team으로 board를 불러오고 다시 board와 member로 feedbackStatus들을 불러오면 됨.
//그러나 boardList controller와 feedbackStatus의 코드 중복성이 문제
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="http://localhost:8081")
public class FeedbackStatusController {
    private final FeedbackStatusService feedbackStatusService;
    @GetMapping("/status/{memberId}/{teamId}")
    public ResponseEntity<Response<FeedbackStatusResponse>> getFeedbackStatus(@PathVariable("memberId") Long memberId,
                                                                              @PathVariable("teamId") Long teamId) {

        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, feedbackStatusService.countFeedbacksForMemberAndTeam(memberId, teamId)));
    }


}


