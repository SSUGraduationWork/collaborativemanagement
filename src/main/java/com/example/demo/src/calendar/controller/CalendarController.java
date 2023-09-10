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
        boolean check = dashboardService.checkDate(form.getDate()); //check==true일 경우 DB에 해당 date 존재
        if (check == false) {                                     //Minutes2 DB에 해당 date 존재하지 않을 경우, 해당 정보로 minutes 생성
            Minutes2 target = dashboardService.createMinutes(form);
            ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", target);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }
        else {                                                    //Minutes2 DB에 해당 date 존재할 경우, 기존의 minutes 보여줌
            Minutes2 original = dashboardService.getExistingMinutes(form.getDate());
            ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", original);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }
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

        //2. 그에 맞게 처리
        boolean check = dashboardService.checkDate(form.getDate()); //check==true일 경우 DB에 해당 date 존재
        if (check != true) {
            throw new DateNotFoundException("Date Not Found");
        }
        MinutesForm minutes = dashboardService.editMinutes(form.getDate(), form);

        //3. 수정한 회의록에 not null이 존재할 경우 오류 발생
        boolean formcheck = dashboardService.checkMinutesForm(minutes);
        if (formcheck == false) {
            throw new FormatBadRequestException("Fill In All");
        }

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", minutes);
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
        boolean check = dashboardService.checkDate(date); //check==true일 경우 DB에 해당 date 존재
        if (check != true) {
            throw new DateNotFoundException(String.format("Date[%s] Not Found", date));
        }
        dashboardService.deleteMinutes(date);
        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //2: dashboard
    //2-1. project 생성
    @PostMapping("/dashboard/projects")
    public ResponseEntity<ResponseData> createProjects(@RequestBody ProjectsForm form) {
        //1. 원하는 정보가 모두 들어오지 않았을 경우 오류 발생
        boolean formcheck = dashboardService.checkProjectsForm(form);
        if (formcheck == false) {
            throw new FormatBadRequestException("Fill In All");
        }

        //2. member table에 해당 id가 있는지 확인 + 교수가 맞는지 확인
        boolean membercheck = dashboardService.checkMember(form.getProfessorId());
        if (membercheck == false) {
            throw new MemberNotFoundException("Member Not Found");
        }

        String professorcheck = dashboardService.checkRole(form.getProfessorId());

        if (!professorcheck.equals("professor")) {      //자바에서 문자열의 경우 ==이 아니라 equals 사용
            throw new MemberNotFoundException("Not Professor");
        }

        //3. 동일한 projectName 존재 확인
        boolean check = dashboardService.checkProjectName(form.getProjectName());

        //(1)동일한 projectName이 존재하지 않을 경우, project 생성
        if (check == false) {
            Projects2 projects2 = dashboardService.createProjects(form);
            ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", projects2);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }
        //(2).Projects2 DB에 해당 projectName 존재할 경우, 기존의 Projects2 보여줌
        else {
            Projects2 original = dashboardService.getExistingProjects(form.getProjectName());
            ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", original);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }
    }

    //2-2. project 조회: 교수별로 => Dashboard:교수 기본 화면
    @GetMapping("/dashboard/projects/{professorId}")
    public ResponseEntity<ResponseData> watchProjects(@PathVariable Long professorId) {
        //1. member table에 해당 id가 있는지 확인 + 교수가 맞는지 확인
        boolean membercheck = dashboardService.checkMember(professorId);
        if (membercheck == false) {
            throw new MemberNotFoundException("Member Not Found");
        }
        String professorcheck = dashboardService.checkRole(professorId);
        if (!professorcheck.equals("professor")) {
            throw new MemberNotFoundException("Not Professor");
        }

        //2. 조회
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
        //입력에 원하는 정보가 들어왔는지 확인
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
        //1. Team form의 not null이 모두 들어왔는지 확인
        boolean formcheck = dashboardService.checkTeamsForm(form);
        if (formcheck == false) {
            throw new FormatBadRequestException("Fill In All");
        }

        //2. Members2 테이블에 존재하는지 + role이 학생인지 확인
        boolean membercheck = dashboardService.checkMember(studentId);
        if (membercheck == false) {
            throw new MemberNotFoundException("Member Not Found");
        }
        String professorcheck = dashboardService.checkRole(studentId);
        if (!professorcheck.equals("student")) {
            throw new MemberNotFoundException("Not Student");
        }

        //3. 해당 projectId가 project table에 존재하는지 확인. 없으면 오류 발생 => ProjectNotFoundException
        boolean check = dashboardService.checkProjectId(form.getProjectId()); //check==true일 경우 DB에 해당 date 존재
        if (check == false) {
            throw new ProjectNotFoundException("Project Not Found");
        }

        //4. teamRepository에서 teamName이 겹치지 않는지 확인
        boolean checkTeamName = dashboardService.checkTeamName(form.getTeamName());

        if (checkTeamName == true) {
            throw new AlreadyExistException("Team Already Exist");
        }

        //1. 팀생성
        Teams2 teams2 = dashboardService.createTeams(form);
        //2. 팀멤버 테이블 추가
        dashboardService.createTeamsMembers(teams2.getTeamId(), studentId);

        //3. 프로젝트와 팀 count 1씩
        Teams2 target = dashboardService.countNums(teams2.getTeamId(), teams2.getProjectId());

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", target);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //2-5. 1) 학생이 보낸 URL(team으로 직접 들어올 경우) => team_number 1 증가 + project_number 1 증가
    @PatchMapping("/dashboard/{projectId}/{teamId}/{userId}")
    public ResponseEntity<ResponseData> countNumbers(@PathVariable Long projectId, @PathVariable Long teamId, @PathVariable Long userId) {
        //1. Members2 테이블에 존재하는지 + role이 학생인지 확인
        boolean membercheck = dashboardService.checkMember(userId);
        if (membercheck == false) {
            throw new MemberNotFoundException("Member Not Found");
        }
        String professorcheck = dashboardService.checkRole(userId);
        if (!professorcheck.equals("student")) {
            throw new MemberNotFoundException("Not Student");
        }

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
//        //1. 교수인지 권한 확인
//        boolean membercheck = dashboardService.checkMember(professorId);
//        if (membercheck == false) {
//            throw new MemberNotFoundException("Member Not Found");
//        }
//        String professorcheck = dashboardService.checkRole(professorId);
//        if (!professorcheck.equals("professor")) {
//            throw new MemberNotFoundException("Not Professor");
//        }
//        //2. 해당 프로젝트 생성자가 해당 professorId인지 확인
//        boolean match = dashboardService.matchProfessorAndProject(professorId, projectId);
//        if (match == false) {
//            throw new ProjectNotFoundException("Matching Project Not Found");
//        }

        //3. 처리
        List<Teams2> teams2List = dashboardService.watchTeamsByPro(projectId);
        if (teams2List.isEmpty()) {
            return null;
        }
        String projectName = dashboardService.findProjectName(projectId);

        ResultForm resultForm = new ResultForm(teams2List, projectName);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", resultForm);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //2-4. team 조회: 학생Id 별로 해당하는 모든 팀 조회 => 학생 기본 화면
    @GetMapping("/dashboard/teamsByStu/{id}")
    public ResponseEntity<ResponseData> watchTeamsByStu(@PathVariable Long id) {
        //1. 해당 id가 member table에 존재하는지
        boolean check = dashboardService.checkMember(id);
        if (check == false) {
            throw new MemberNotFoundException("Member Not Found");
        }
        //2. 학생인지 권한 확인
        String professorcheck = dashboardService.checkRole(id);
        if (!professorcheck.equals("student")) {
            throw new MemberNotFoundException("Not Professor");
        }
        //3. 처리
        List<Teams2> teams2List = dashboardService.watchTeamsByStu(id);
        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", teams2List);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //2-5. team 수정
    @PatchMapping("/dashboard/teams")
    public ResponseEntity<ResponseData> updateTeams(@RequestBody TeamsForm form) {
        Teams2 teams = dashboardService.editTeams(form);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", teams);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //2-6. teams 삭제
    @DeleteMapping("/dashboard/teams/{teamId}")
    public ResponseEntity<ResponseData> deleteTeams(@PathVariable Long teamId) {
        dashboardService.deleteTeams(teamId);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

}
