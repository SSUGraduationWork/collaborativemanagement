package com.example.demo.src.calendar.service;

import com.example.demo.src.calendar.dto.MinutesForm;
import com.example.demo.src.calendar.dto.ProjectsForm;
import com.example.demo.src.calendar.dto.TeamsForm;
import com.example.demo.src.calendar.entity.*;
import com.example.demo.src.calendar.repository.*;
import com.example.demo.src.file.domain.Teams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardService {
    private final Members2Repository members2Repository;
    private final Projects2Repository projects2Repository;
    private final Teams2Repository teams2Repository;

    private final TeamMembers2Repository teamMembers2Repository;
    private  final Minutes2Repository minutes2Repository;

    //확인
    //1-1. minutes not null 데이터를 모두 입력받았는지
    @Transactional
    public boolean checkMinutesForm(MinutesForm form) {
        boolean checkform = true;
        if (form.getTeamId() == null) {
            checkform = false;
            return checkform;
        } else if (form.getUserId() == null) {
            checkform = false;
            return checkform;
        } else if (form.getDate() == null) {
            checkform = false;
            return checkform;
        }
        return checkform;
    }
    //1-2. minutesRepository에 해당 date가 존재하는지
    @Transactional
    public boolean checkDate(String date) {
        boolean check = false;
        if (minutes2Repository.findByDate(date) != null) {
            check = true;
        }
        return check;
    }

    //2-1. not null 데이터를 모두 입력받았는지
    @Transactional
    public boolean checkProjectsForm(ProjectsForm form) {
        boolean checkform = true;
        if (form.getProfessorId() == null) {
            checkform = false;
            return checkform;
        } else if (form.getProjectName() == null) {
            checkform = false;
            return checkform;
        } else if (form.getSemester() == null) {
            checkform = false;
            return checkform;
        }
        return checkform;
    }


    @Transactional
    public boolean checkTeamsForm(TeamsForm form) {
        boolean checkform = true;
        if (form.getProjectId() == null) {
            checkform = false;
            return checkform;
        } else if (form.getTeamName() == null) {
            checkform = false;
            return checkform;
        }
        return checkform;
    }


    //2-2. 해당 project가 존재하는지: projectName으로
    @Transactional
    public boolean checkProjectName(String projectName) {
        boolean check = false;
        if (projects2Repository.findByProjectName(projectName) != null) {
            check = true;
        }
        return check;
    }

    @Transactional
    public boolean checkProjectIdAndTeamId(Long projectId, Long teamId) {
        boolean check = false;
        if (projects2Repository.findById(projectId) != null || teams2Repository.findById(teamId) != null) {
            check = true;
        }
        return check;
    }
    //2-3. 해당 project가 존재하는지: projectId로
    @Transactional
    public boolean checkProjectId(Long projectId) {
        boolean check = false;
        if (projects2Repository.findById(projectId) != null) {
            check = true;
        }
        return check;
    }
    //2-4. projectId와 일치하는 team이 존재하는지
    @Transactional
    public boolean checkTeam(Long teamId) {
        boolean check = false;
        if (teams2Repository.findById(teamId) != null) {
            check = true;
        }
        return check;
    }
    @Transactional
    public boolean checkTeamName(String teamName) {
        boolean check = false;
        if (teams2Repository.findByTeamName(teamName) != null) {
            check = true;
        }
        return check;
    }



    //2-5. team에 해당 사용자가 존재하는지
    @Transactional
    public boolean checkTeamMember(Long teamId, Long userId) {
        boolean check = false;
        if (teamMembers2Repository.findByTeamIdAndUserId(teamId, userId) != null) {
            check = true;
        }
        return check;
    }

    //2-6. memberRepository에 해당 사용자가 존재하는지
    @Transactional
    public boolean checkMember(Long userId) {
        boolean check = false;
        if (members2Repository.findById(userId) != null) {
            check = true;
        }
        return check;
    }

    @Transactional
    public String checkRole(Long userId) {
        String check = "";
        Members2 members2 = members2Repository.findById(userId).orElse(null);
        check = members2.getRole();
        return check;
    }

    //3. 기능
    //minutes
    @Transactional
    public Minutes2 getExistingMinutes(String date) {
        Minutes2 original = minutes2Repository.findByDate(date);
        return original;
    }

    @Transactional
    public Minutes2 createMinutes(MinutesForm dto) {

        Minutes2 minutes2 = minutes2Repository.save(dto.toEntity(dto));
        Minutes2 target = minutes2Repository.findById(minutes2.getMinutesId()).orElse(null);
        return target;
    }

    @Transactional
    public List<Minutes2> watchAll(Long teamId) {
        List<Minutes2> minutes2List = minutes2Repository.findByTeamId(teamId);
        return minutes2List;
    }

    @Transactional
    public List<Minutes2> watchDates(Long teamId, String filterMonth) {
        List<Minutes2> target = new ArrayList<>();
        List<Minutes2> minutes2List = minutes2Repository.findByTeamId(teamId);

        for (Minutes2 minutes2 : minutes2List) {
            if (minutes2.getDate().startsWith(filterMonth)) {
                target.add(minutes2);
            }
        }
        return target;
    }
    @Transactional
    public Minutes2 watchDate(String date) {

        Minutes2 minutes2 = minutes2Repository.findByDate(date);
        return minutes2;
    }

    @Transactional
    public Minutes2 findMinute(Long teamId, String date) {
        Minutes2 target = null;
        List<Minutes2> minutes2List = minutes2Repository.findByTeamId(teamId);

        for (Minutes2 minutes2 : minutes2List) {
            if (minutes2.getDate().equals(date)) {
                target = minutes2;
            }
        }
        return target;
    }
    @Transactional
    public List<Integer> findMinuteByDate(Long teamId, String currentMonth) {
        List<Integer> resultList = new ArrayList<>();
        List<Minutes2> teamMinuteList = minutes2Repository.findByTeamId(teamId);
        for (Minutes2 minutes2: teamMinuteList) {
            if (String.valueOf(minutes2.getDate()).startsWith(currentMonth)) {
                String[] parts = minutes2.getDate().toString().split("-");
                String lastDigit = parts[2];
                Integer date = Integer.valueOf(lastDigit);
                resultList.add(date);
            }
        }
        return resultList;
    }
    @Transactional
    public MinutesForm editMinutes(Minutes2 minutes2, MinutesForm dto) {
        //현재 입력한 date가 repository에 존재할 경우 edit. orElseThrow()

        if (dto.getTitle() != null) {
            minutes2.updateTitle(dto.getTitle());
        }

        if (dto.getContent() != null) {
            minutes2.updateContent(dto.getContent());
        }

        if (dto.getUserId() != minutes2.getUserId()) {
            minutes2.updateUserId(dto.getUserId());
        }
        MinutesForm dtotarget = minutes2.toDto(minutes2);

        return dtotarget;
    }

    @Transactional
    public void deleteMinutes(Minutes2 minutes2) {
        minutes2Repository.delete(minutes2);
    }

    //프로젝트+팀
    @Transactional
    public List<Members2> watchMembers() {
        return members2Repository.findAll();
    }

    @Transactional
    public List<Projects2> watchProjects() {
        return projects2Repository.findAll();
    }

    @Transactional
    public List<Teams2> watchTeams() {
        return teams2Repository.findAll();
    }

    @Transactional
    public Projects2 getExistingProjects(String projectName) {
        Projects2 original = projects2Repository.findByProjectName(projectName);
        return original;
    }

    @Transactional
    public Projects2 createProjects(ProjectsForm dto) {
        Projects2 projects2 = projects2Repository.save(dto.toEntity(dto));
        return projects2;
    }

    @Transactional
    public List<Projects2> watchProjects(Long professor_id) {
        List<Projects2> projects2List = projects2Repository.findByProfessorId(professor_id);
        return projects2List;
    }

    @Transactional
    public Projects2 editProjects(ProjectsForm dto) {
        Projects2 project = projects2Repository.findById(dto.getProjectId()).orElse(null);
        if (dto.getProjectName() != null) {
            if (dto.getSemester() != null) {
                project.setProjectName(dto.getProjectName());
                project.setSemester(dto.getSemester());
            }
        }
        return project;
    }

    @Transactional
    public void deleteProjects(Long projectId) {
        Projects2 project = projects2Repository.findById(projectId).orElse(null);

        projects2Repository.delete(project);
    }

    @Transactional
    public List<Projects2> watchProjectsBySemester(Long professor_id, String semester) {
        List<Projects2> projects2List = projects2Repository.findByProfessorIdAndSemester(professor_id, semester);

        return projects2List;
    }
    @Transactional
    public Teams2 createTeams(TeamsForm dto) {
        Teams2 teams2 = teams2Repository.save(dto.toEntity(dto));
        return teams2;
    }


//    //팀멤버테이블에 추가 -> 미완성
    @Transactional
    public void createTeamsMembers(Long teamId, Long userId) {
        TeamMembers2 teamMembers2 = new TeamMembers2();
        teamMembers2.setTeamId(teamId);
        teamMembers2.setUserId(userId);
        teamMembers2.setContribution(0L);

        teamMembers2Repository.save(teamMembers2);
    }

    @Transactional
    public Teams2 countNums(Long teamId, Long projectId) {
        Projects2 projects2 = projects2Repository.findById(projectId).orElse(null);
        projects2.setProjectNumber(projects2.getProjectNumber() + 1);
        Teams2 teams2 = teams2Repository.findByTeamIdAndProjectId(teamId, projectId);
        teams2.setTeamNumber(teams2.getTeamNumber() + 1);
        return teams2;
    }

    @Transactional
    public List<Teams2> watchTeamsByPro(Long projectId) {
        List<Teams2> teams2List = teams2Repository.findByProjectId(projectId);
        return teams2List;
    }

    @Transactional
    public List<Teams2> watchTeamsByStu(Long id) {
        List<Long> teamsId = new ArrayList<>();
        List<Teams2> teams2List = new ArrayList<>();

        List<TeamMembers2> teamMembers2List = teamMembers2Repository.findByUserId(id);

        int cnt = teamMembers2List.size();

        for (int i=0; i<cnt; i++) {
            TeamMembers2 teamMembers2 = teamMembers2List.get(i); //객체 전체 선택
            teamsId.add(teamMembers2.getTeamId());             //각 객체에서 teamId만 teamsId에 넣기
        }

        int count = teamsId.size();

        for (int j=0; j<count; j++) {
            Teams2 teams2 = teams2Repository.findById(teamsId.get(j)).orElse(null);
            teams2List.add(teams2);
        }

        return teams2List;
    }

    @Transactional
    public boolean matchProfessorAndProject(Long professorId, Long projectId) {
        boolean check = false;
        Projects2 projects2 = projects2Repository.findByProfessorIdAndProjectId(professorId, projectId);
        if (projects2 != null) {
            check = true;
        }
        return check;
    }
    @Transactional
    public String findProjectName(Long projectId) {
        Projects2 projects2 = projects2Repository.findById(projectId).orElse(null);
        String projectName = projects2.getProjectName();

        return projectName;
    }

    @Transactional
    public List<Projects2> findProjectInfo(List<Teams2> teams2List) {
        List<Projects2> projects2List = new ArrayList<>();

        for (Teams2 teams2: teams2List) {
            Long projectId = teams2.getProjectId();
            projects2List.add(projects2Repository.findById(projectId).orElse(null));
        }

        return projects2List;
    }

    @Transactional
    public List<Teams2> findTeamBySemester(List<Teams2> teams2List, String semester) {
        List<Teams2> resultTeamList = new ArrayList<>();
        Long projectId = null;

        for (Teams2 teams2:teams2List) {
            projectId = teams2.getProjectId();
            log.info("projectId: "+ teams2.toString());
            log.info("project"+ teams2.getProjectId().toString());
            Projects2 projects2 = projects2Repository.findById(projectId).orElse(null);
            log.info("projects2: "+projects2.toString());
            log.info("projects2.getSemester: "+ projects2.getSemester().toString()+ "semester: "+ semester.toString());
            if (projects2.getSemester().equals(semester)) {
                resultTeamList.add(teams2);
            }
        }
        log.info("resultTeamList: "+resultTeamList.toString());

        return resultTeamList;
    }

    @Transactional
    public Teams2 editTeams(TeamsForm dto) {
        Teams2 team = teams2Repository.findByTeamId(dto.getTeamId());

        if (dto.getTeamName() != null) {
            team.setTeamName(dto.getTeamName());
        }
        return team;
    }

    @Transactional
    public void deleteTeams(Long teamId) {
        Teams2 team = teams2Repository.findByTeamId(teamId);

        teams2Repository.delete(team);
    }

}
