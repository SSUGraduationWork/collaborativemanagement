package com.example.graduation.controller;

import com.example.graduation.dto.MinutesForm;
import com.example.graduation.entity.Minutes;
import com.example.graduation.entity.Users;
import com.example.graduation.repository.UsersRepository;
import com.example.graduation.service.MinutesService;
import com.example.graduation.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j //log
@RequiredArgsConstructor
public class CalendarController {

    private final MinutesService minutesService;
    private final UsersService usersService;

    //1. 회의록 생성
    @PostMapping("/calendars/minutes")
    public MinutesForm createMinutes(@RequestBody MinutesForm form) {
        MinutesForm target = minutesService.create(form);
        return target;
    }

    //2-1. 회의록 조회(전체)
    @GetMapping("/calendars/minutes/all")
    public List<Minutes> getAllMinutes() {
        List<Minutes> minutesList = minutesService.watchAll();
        return minutesList;
    }

    // 2-2. 회의록 부분 조회(특정 년도, 월)
    @GetMapping("/calendars/minutes/all/{yearMonth}")
    public List<Minutes> getAllMinutes(@PathVariable String yearMonth) {
        List<Minutes> minutesList = minutesService.watchDates(yearMonth);
        if (minutesList.isEmpty()) {
            return null;
        }
        else {
            return minutesList;
        }
    }

    //2-2. 회의록 조회(세부)
    @GetMapping("/calendars/minutes/{date}")
    public Optional<Minutes> getMinutes(@PathVariable String date) {
        Optional<Minutes> minutes = minutesService.watch(date);
        return minutes;
    }


    //3. 회의록 수정
    @PatchMapping("/calendars/minutes/{date}")
    public MinutesForm editMinutes(@PathVariable String date, @RequestBody MinutesForm form) {
        MinutesForm minutes = minutesService.edit(date, form);
        return minutes;
    }
//
    //4. 회의록 삭제
    @DeleteMapping("/calendars/minutes/{date}")
    public String deleteMinutes(@PathVariable String date) {
        minutesService.delete(date);
        return "Completely Deleted";
    }

}
