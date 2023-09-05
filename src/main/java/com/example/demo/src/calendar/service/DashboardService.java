package com.example.demo.src.calendar.service;

import com.example.demo.src.calendar.dto.MinutesForm;
import com.example.demo.src.calendar.dto.ProjectsForm;
import com.example.demo.src.calendar.dto.TeamsForm;
import com.example.demo.src.calendar.entity.*;
import com.example.demo.src.calendar.repository.*;
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
    public MinutesForm editMinutes(String date, MinutesForm dto) {
        //현재 입력한 date가 repository에 존재할 경우 edit. orElseThrow()
        Minutes2 minutes2 = minutes2Repository.findByDate(date);
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
    public void deleteMinutes(String date) {
        Minutes2 minutes2 = minutes2Repository.findByDate(date);
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
        log.info("teamsId:"+ teamsId);

        int count = teamsId.size();

        for (int j=0; j<count; j++) {
            Teams2 teams2 = teams2Repository.findById(teamsId.get(j)).orElse(null);
            teams2List.add(teams2);
        }

        log.info("teams2List:"+ teams2List);
        return teams2List;
    }

    @Transactional
    public boolean matchProfessorAndProject(Long professorId, Long projectId) {
        boolean check = false;
        Projects2 projects2 = projects2Repository.findByProfessorIdAndId(professorId, projectId);
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

//    @Transactional
//    public TeamMembers2 addTeamMember(Long projectId ,Long teamId, Long userId) {
//        TeamMembers2 entity = new TeamMembers2();
//        entity.setTeamId(teamId);
//        entity.setUserId(userId);
//        teamMembers2Repository.save(entity);
//        return entity;
//    }

//    @Transactional
//    public List<Teams2> getTeamsInProjects(Long projectId) {
//        Projects2 projects = projects2Repository.findByProjectId(projectId);
//        log.info(projects.toString());
//        log.info(projects.getTeams().toString());
//        if (projects != null) {
//            return projects.getTeams();
//        }
//        else {
//            return Collections.emptyList();
//        }
//      }



}
