package com.example.demo.src.calendar.controller;

import com.example.demo.src.calendar.Exception.*;
import com.example.demo.src.calendar.dto.MinutesForm;
import com.example.demo.src.calendar.dto.ProjectsForm;
import com.example.demo.src.calendar.dto.ResultForm;
import com.example.demo.src.calendar.dto.TeamsForm;
import com.example.demo.src.calendar.entity.Minutes2;
import com.example.demo.src.calendar.entity.Projects2;
import com.example.demo.src.calendar.entity.Teams2;
import com.example.demo.src.calendar.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j //log
@RequiredArgsConstructor
@CrossOrigin(origins = "*") //CORS
public class CalendarController {

//    private final MinutesService minutesService;
    private final DashboardService dashboardService;

    private class ResponseData {
        private int status;
        private String message;
        private Object data;

        public ResponseData(int status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }

    //회의록에서는 팀멤버임을 확인했다고 가정한 후의 API: team member임을 확인하는 과정 X
    //1: Calendar Minutes2
    //1-1. 회의록 생성: 해당 날짜의 회의록이 존재할 경우 해당 회의록 보여주기
    @PostMapping("/calendars/minutes")
    public ResponseEntity<ResponseData> createMinutes(@RequestBody MinutesForm form) {

        //1. 원하는 정보가 모두 들어오지 않았을 경우 오류 발생
        boolean formcheck = dashboardService.checkMinutesForm(form);
        if (formcheck == false) {
            throw new FormatBadRequestException("Fill In All");
        }

        //2. teamId가 존재하는지 + 해당 team member에 사용자가 존재하는지
        boolean teamcheck = dashboardService.checkTeam(form.getTeamId());
        if (teamcheck == false) {
            throw new TeamNotFoundException("Team Not Found");
        }
        boolean teamMembercheck = dashboardService.checkTeamMember(form.getTeamId(), form.getUserId());
        if (teamMembercheck == false) {
            throw new MemberNotFoundException("Member Not Found");
        }

        //3. DB에 해당 date의 회의록이 존재하는지 여부에 따라 다르게 처리
//        boolean check = dashboardService.checkDate(form.getDate()); //check==true일 경우 DB에 해당 date 존재
//        if (check == false) {                                     //Minutes2 DB에 해당 date 존재하지 않을 경우, 해당 정보로 minutes 생성
//            Minutes2 target = dashboardService.createMinutes(form);
//            ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", target);
//            return new ResponseEntity<>(responseData, HttpStatus.OK);
//        }
//        else {                                                    //Minutes2 DB에 해당 date 존재할 경우, 기존의 minutes 보여줌
//            Minutes2 original = dashboardService.getExistingMinutes(form.getDate());
//            ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", original);
//
//        }

        //4. 팀아이디에 해당하는 회의록 생성
        Minutes2 minutes = dashboardService.createMinutes(form);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", minutes);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //1-2(1). 회의록 조회(전체)
    //조회할 것이 없을 경우 null 출력
    @GetMapping("/calendars/minutes/all/{teamId}/{userId}")
    public ResponseEntity<ResponseData> getAllMinutes(@PathVariable Long teamId, @PathVariable Long userId) {
        //userId가 해당팀의 멤버인지 확인

        //전체 조회
        List<Minutes2> minutes2List = dashboardService.watchAll(teamId);
        if (minutes2List.isEmpty()) {
            return null;
        }
        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", minutes2List);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //1-2(2). 회의록 부분 조회(특정 년도, 월)
    //조회할 것이 없을 경우 null 출력
    @GetMapping("/calendars/minutes/all/{teamId}/{yearMonth}/{userId}")
    public ResponseEntity<ResponseData> getAllMinutes(@PathVariable Long teamId,@PathVariable String yearMonth, @PathVariable Long userId) {
        //userId가 해당팀의 멤버인지 확인


        // 부분 조회
        List<Minutes2> minutes2List = dashboardService.watchDates(teamId, yearMonth);
        if (minutes2List == null) {
            return null;
        }
        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", minutes2List);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //1-2(3). 회의록 조회(세부)
    //조회할 것이 없을 경우 null 출력
    @GetMapping("/calendars/minutes/{teamId}/{date}/{userId}")
    public ResponseEntity<ResponseData> getMinutes(@PathVariable Long teamId, @PathVariable String date, @PathVariable Long userId) {

        //해당 teamId의 회의록 모두 찾기
        Minutes2 minute= dashboardService.findMinute(teamId, date);
        log.info("get minutes2: ", minute);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", minute);

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
    //1-2(4). 회의록이 존재하는 모든 날짜리스트
    @GetMapping("/calendars/getExistMinutes/{teamId}/{currentMonth}")
    public ResponseEntity<ResponseData> getExistDate(@PathVariable Long teamId, @PathVariable String currentMonth) {
        //팀아이디에 해당하는 모든 minute 찾고 date 비교
        List<String> list = dashboardService.findMinuteByDate(teamId, currentMonth);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", list);

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //1-3. 회의록 수정 => 수정할때
    //해당 날짜가 존재하지 않을 경우 오류 발생
    @PatchMapping("/calendars/minutes")
    public ResponseEntity<ResponseData> editMinutes(@RequestBody MinutesForm form) {
        //수정이므로 아래 과정은 없어도 될듯
//        //1. 원하는 정보가 모두 들어오지 않았을 경우 오류 발생
//        boolean formcheck = dashboardService.checkMinutesForm(form);
//        if (formcheck == false) {
//            throw new FormatBadRequestException("Fill In All");
//        }

        //1. teamId가 존재하는지 + 해당 team member에 사용자가 존재하는지
        boolean teamcheck = dashboardService.checkTeam(form.getTeamId());
        if (teamcheck == false) {
            throw new TeamNotFoundException("Team Not Found");
        }
        boolean teamMembercheck = dashboardService.checkTeamMember(form.getTeamId(), form.getUserId());
        if (teamMembercheck == false) {
            throw new MemberNotFoundException("Member Not Found");
        }

//        //2. 그에 맞게 처리
//        boolean check = dashboardService.checkDate(form.getDate()); //check==true일 경우 DB에 해당 date 존재
//        if (check != true) {
//            throw new DateNotFoundException("Date Not Found");
//        }
//        MinutesForm minutes = dashboardService.editMinutes(form.getDate(), form);

//        //3. 수정한 회의록에 not null이 존재할 경우 오류 발생
//        boolean formcheck = dashboardService.checkMinutesForm(minutes);
//        if (formcheck == false) {
//            throw new FormatBadRequestException("Fill In All");
//        }
        Minutes2 minutes2 = dashboardService.findMinute(form.getTeamId(), form.getDate());
        MinutesForm dtotarget = dashboardService.editMinutes(minutes2, form);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", dtotarget);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //1-4. 회의록 삭제
    //해당 날짜가 존재하지 않을 경우 오류 발생
    @DeleteMapping("/calendars/minutes/{teamId}/{userId}/{date}")
    public ResponseEntity<ResponseData> deleteMinutes(@PathVariable Long teamId,@PathVariable Long userId, @PathVariable String date) {
        //1. 원하는 정보가 모두 들어오지 않았을 경우 오류 발생
//        boolean formcheck = dashboardService.checkMinutesForm(form);
//        if (formcheck == false) {
//            throw new FormatBadRequestException("Fill In All");
//        }

        //2. teamId가 존재하는지 + 해당 team member에 사용자가 존재하는지
        boolean teamcheck = dashboardService.checkTeam(teamId);
        if (teamcheck == false) {
            throw new TeamNotFoundException("Team Not Found");
        }
        boolean teamMembercheck = dashboardService.checkTeamMember(teamId, userId);
        if (teamMembercheck == false) {
            throw new MemberNotFoundException("Member Not Found");
        }

        //3. 처리
//        boolean check = dashboardService.checkDate(date); //check==true일 경우 DB에 해당 date 존재
//        if (check != true) {
//            throw new DateNotFoundException(String.format("Date[%s] Not Found", date));
//        }
//

        Minutes2 minutes2 = dashboardService.findMinute(teamId, date);
        dashboardService.deleteMinutes(minutes2);
        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

}
