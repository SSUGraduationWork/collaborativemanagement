package com.example.demo.src.calendar.controller;

import com.example.demo.src.calendar.Exception.*;
import com.example.demo.src.calendar.dto.*;
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

        //4. 팀아이디에 해당하는 회의록 생성
        Minutes2 minutes = dashboardService.createMinutes(form);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", minutes);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //1-2(1). 회의록 조회(전체)
    //조회할 것이 없을 경우 null 출력
    @GetMapping("/calendars/minutes/all/{teamId}/{userId}")
    public ResponseEntity<ResponseData> getAllMinutes(@PathVariable Long teamId, @PathVariable Long userId) {


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
        List<Integer> list = dashboardService.findMinuteByDate(teamId, currentMonth);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", list);

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //1-3. 회의록 수정 => 수정할때
    //해당 날짜가 존재하지 않을 경우 오류 발생
    @PatchMapping("/calendars/minutes")
    public ResponseEntity<ResponseData> editMinutes(@RequestBody MinutesForm form) {
        Minutes2 minutes2 = dashboardService.findMinute(form.getTeamId(), form.getDate());
        MinutesForm dtotarget = dashboardService.editMinutes(minutes2, form);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", dtotarget);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //1-4. 회의록 삭제
    //해당 날짜가 존재하지 않을 경우 오류 발생
    @DeleteMapping("/calendars/minutes/{teamId}/{userId}/{date}")
    public ResponseEntity<ResponseData> deleteMinutes(@PathVariable Long teamId,@PathVariable Long userId, @PathVariable String date) {

        Minutes2 minutes2 = dashboardService.findMinute(teamId, date);
        dashboardService.deleteMinutes(minutes2);
        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //2: dashboard
    //2-1. project 생성
    @PostMapping("/dashboard/projects")
    public ResponseEntity<ResponseData> createProjects(@RequestBody ProjectsForm form) throws AlreadyExistException {
        //1. 원하는 정보가 모두 들어오지 않았을 경우 오류 발생
        boolean formcheck = dashboardService.checkProjectsForm(form);
        if (formcheck == false) {
            throw new FormatBadRequestException("Fill In All");
        }

        //2. 동일한 projectName 존재 확인
        boolean check = dashboardService.checkProjectName(form.getProjectName());

        //(1)동일한 projectName이 존재하지 않을 경우, project 생성
        if (check == false) {
            Projects2 projects2 = dashboardService.createProjects(form);
            ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", projects2);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }
        //(2).Projects2 DB에 해당 projectName 존재할 경우, 기존의 Projects2 보여줌
        else {
            throw new AlreadyExistException("Project Already Exist");
        }
    }

    //2-2. project 조회: 교수별로 => Dashboard:교수 기본 화면
    @GetMapping("/dashboard/projects/{professorId}")
    public ResponseEntity<ResponseData> watchProjects(@PathVariable Long professorId) {
        List<Projects2> projects2List = dashboardService.watchProjects(professorId);
        if (projects2List.isEmpty()) {
            return null;
        }
        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", projects2List);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //2-3. project 수정
    @PatchMapping("/dashboard/projects")
    public ResponseEntity<ResponseData> editProject(@RequestBody ProjectsForm form) {
        Projects2 projects = dashboardService.editProjects(form);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", projects);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //2-4. project 삭제
    @DeleteMapping("/dashboard/projects/{projectId}")
    public ResponseEntity<ResponseData> deleteProject(@PathVariable Long projectId) {
        dashboardService.deleteProjects(projectId);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //semester에 따라 대시보드에 보이는 프로젝트 다름
    @GetMapping("/dashboard/projects/{professorId}/{semester}")
    public ResponseEntity<ResponseData> watchProjectsBySemester(@PathVariable Long professorId, @PathVariable String semester) {
        List<Projects2> projects2List = dashboardService.watchProjectsBySemester(professorId, semester);

        if (projects2List.isEmpty()) {
            return null;
        }

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", projects2List);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //2-3. team 생성 => 학생. Dashboard에서 생성할 것
    @PostMapping("/dashboard/teams/{studentId}")
    public ResponseEntity<ResponseData> createTeams(@RequestBody TeamsForm form, @PathVariable Long studentId) throws AlreadyExistException {
        //1. teamRepository에서 teamName이 겹치지 않는지 확인
        boolean checkTeamName = dashboardService.checkTeamName(form.getTeamName());

        if (checkTeamName == true) {
            throw new AlreadyExistException("Team Already Exist");
        }

        //2. 팀생성
        Teams2 teams2 = dashboardService.createTeams(form);
        //3. 팀멤버 테이블 추가
        dashboardService.createTeamsMembers(teams2.getTeamId(), studentId);

        //4. 프로젝트와 팀 count 1씩
        Teams2 target = dashboardService.countNums(teams2.getTeamId(), teams2.getProjectId());

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", target);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //2-5. 1) 학생이 보낸 URL(team으로 직접 들어올 경우) => team_number 1 증가 + project_number 1 증가
    @PatchMapping("/dashboard/{projectId}/{teamId}/{userId}")
    public ResponseEntity<ResponseData> countNumbers(@PathVariable Long projectId, @PathVariable Long teamId, @PathVariable Long userId) {
        //2. 해당 projectId가 project table에 존재하는지 확인. 없으면 오류 발생 => ProjectNotFoundException
        boolean checkProject = dashboardService.checkProjectId(projectId); //check==true일 경우 DB에 해당 date 존재
        if (checkProject == false) {
            throw new ProjectNotFoundException("Project Not Found");
        }

        //3. 해당 team이 존재하는지
        boolean checkTeam = dashboardService.checkTeam(teamId);
        if (checkTeam == false) {
            throw new TeamNotFoundException("Team Not Found");
        }

        boolean check = dashboardService.checkProjectIdAndTeamId(projectId, teamId);
        if (check == false) {
            throw new ProjectAndTeamNotFoundException("Project and Team Not Found");
        }

        //4. 이미 멤버 테이블에 존재하는지 확인
        boolean checkMember = dashboardService.checkTeamMember(teamId, userId);
        if (checkMember == false) {     //팀멤버 테이블에 존재하지 않는경우
            //1. 팀멤버 테이블에 추가
            dashboardService.createTeamsMembers(teamId, userId);
            //2. teamnumber, projectnumber 1씩 증가
            Teams2 target = dashboardService.countNums(teamId, projectId);    //team_number 1 증가 + project_number 1 증가

            ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", target);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }
        else {
            ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }
    }

    //2-4. team 조회: project 별로 모든 팀 조회=> 교수, Dashboard에서 프로젝트 클릭시
    @GetMapping("/dashboard/teamsByPro/{projectId}")
    public ResponseEntity<ResponseData> watchTeamsByPro(@PathVariable Long projectId) {
        List<Teams2> teams2List = dashboardService.watchTeamsByPro(projectId);
        if (teams2List.isEmpty()) {
            return null;
        }
        String projectName = dashboardService.findProjectName(projectId);

        ResultForm resultForm = new ResultForm(teams2List, projectName, null);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", resultForm);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //2-4. team 조회: 학생Id 별로 해당하는 모든 팀 조회 => 학생 기본 화면
    @GetMapping("/dashboard/teamsByStu/{id}")
    public ResponseEntity<ResponseData> watchTeamsByStu(@PathVariable Long id) {

        List<Teams2> teams2List = dashboardService.watchTeamsByStu(id);

        List<TeamAndProjectForm> form = dashboardService.getProjectAndTeam(teams2List);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", form);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //2-5. semester에 따라 team 다르게 보이기
    @GetMapping("/dashboard/teams/{studentId}/{semester}")
    public ResponseEntity<ResponseData> watchTeamsBySemester(@PathVariable Long studentId, @PathVariable String semester) {
        List<Teams2> teams2List = dashboardService.watchTeamsByStu(studentId);

        if (teams2List.isEmpty()) {
            return null;
        }

        List<Teams2> resultTeamList = dashboardService.findTeamBySemester(teams2List, semester);
        log.info("semesterResultForm: ", resultTeamList);
        List<Projects2> projects2List = dashboardService.findProjectInfo(resultTeamList);
        log.info("semesterResultForm: ", projects2List);
        ResultForm resultForm = new ResultForm(resultTeamList, null, projects2List);
        log.info("semesterResultForm: ", resultForm);
        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", resultForm);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //2-6. team 수정
    @PatchMapping("/dashboard/teams")
    public ResponseEntity<ResponseData> updateTeams(@RequestBody TeamsForm form) {
        Teams2 teams = dashboardService.editTeams(form);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", teams);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //2-7. teams 삭제
    @DeleteMapping("/dashboard/teams/{teamId}")
    public ResponseEntity<ResponseData> deleteTeams(@PathVariable Long teamId) {
        dashboardService.deleteTeams(teamId);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

}
