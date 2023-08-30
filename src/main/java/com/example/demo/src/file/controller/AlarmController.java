package com.example.demo.src.file.controller;

import com.example.demo.src.file.Repository.AlarmRepository;
import com.example.demo.src.file.Service.AlarmService;
import com.example.demo.src.file.common.CommonCode;
import com.example.demo.src.file.common.Response;
import com.example.demo.src.file.dto.response.AlarmDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins="http://localhost:8081")
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
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, alarmService.alarmList(memberId)));
    }

    @PutMapping("/updateSeenStatus/{alarmId}")
    public ResponseEntity<String> updateSeenStatus(@PathVariable Long alarmId) {
        alarmService.updateSeenStatus(alarmId);
        return ResponseEntity.ok("Seen status updated successfully");
    }
}


