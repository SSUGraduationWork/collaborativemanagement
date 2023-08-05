package com.example.upload.controller;

import com.example.upload.Repository.AlarmRepository;
import com.example.upload.Service.AlarmService;
import com.example.upload.Service.BoardService;
import com.example.upload.common.CommonCode;
import com.example.upload.common.Response;
import com.example.upload.domain.Alarms;
import com.example.upload.dto.response.AlarmDetailResponse;
import com.example.upload.dto.response.BoardResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;
    private final AlarmRepository alarmRepository;
    //알람 자세하게 확인하기
    @GetMapping("/alarm/view/{alarmId}")
    public ResponseEntity<Response<AlarmDetailResponse>>  viewAlarm(@PathVariable Long alarmId) {
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, alarmService.DetailAlarm(alarmId)));
    }

    //멤버별로 알람 리스트 확인하기
    @GetMapping("/alarmList/view/{memberId}")
    public ResponseEntity<Response<List<AlarmDetailResponse>>> AlarmList(
            @PathVariable Long memberId,
            @PageableDefault(page=0, size=10, sort="createdAt", direction=Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, alarmService.alarmList(memberId, pageable)));
    }
}


