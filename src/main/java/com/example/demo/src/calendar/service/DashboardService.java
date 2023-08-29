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
    private final MembersRepository membersRepository;
    private final ProjectsRepository projectsRepository;
    private final TeamsRepository teamsRepository;

    private final TeamMembersRepository teamMembersRepository;
    private  final MinutesRepository minutesRepository;

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
        if (minutesRepository.findByDate(date) != null) {
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
        if (projectsRepository.findByProjectName(projectName) != null) {
            check = true;
        }
        return check;
    }

    //2-3. 해당 project가 존재하는지: projectId로
    @Transactional
    public boolean checkProjectId(Long projectId) {
        boolean check = false;
        if (projectsRepository.findById(projectId) != null) {
            check = true;
        }
        return check;
    }
    //2-4. projectId와 일치하는 team이 존재하는지
    @Transactional
    public boolean checkTeam(Long teamId) {
        boolean check = false;
        if (teamsRepository.findById(teamId) != null) {
            check = true;
        }
        return check;
    }
    @Transactional
    public boolean checkTeamName(String teamName) {
        boolean check = false;
        if (teamsRepository.findByTeamName(teamName) != null) {
            check = true;
        }
        return check;
    }



    //2-5. team에 해당 사용자가 존재하는지
    @Transactional
    public boolean checkTeamMember(Long teamId, Long userId) {
        boolean check = false;
        if (teamMembersRepository.findByTeamIdAndUserId(teamId, userId) != null) {
            check = true;
        }
        return check;
    }

    //2-6. memberRepository에 해당 사용자가 존재하는지
    @Transactional
    public boolean checkMember(Long userId) {
        boolean check = false;
        if (membersRepository.findById(userId) != null) {
            check = true;
        }
        return check;
    }

    @Transactional
    public String checkRole(Long userId) {
        String check = "";
        Members members = membersRepository.findById(userId).orElse(null);
        check = members.getRole();
        return check;
    }

    //3. 기능
    //minutes
    @Transactional
    public Minutes getExistingMinutes(String date) {
        Minutes original = minutesRepository.findByDate(date);
        return original;
    }

    @Transactional
    public Minutes createMinutes(MinutesForm dto) {

        Minutes minutes= minutesRepository.save(dto.toEntity(dto));
        Minutes target = minutesRepository.findById(minutes.getMinutesId()).orElse(null);
        return target;
    }

    @Transactional
    public List<Minutes> watchAll(Long teamId) {
        List<Minutes> minutesList= minutesRepository.findByTeamId(teamId);
        return minutesList;
    }

    @Transactional
    public List<Minutes> watchDates(Long teamId,String filterMonth) {
        List<Minutes> target = new ArrayList<>();
        List<Minutes> minutesList = minutesRepository.findByTeamId(teamId);

        for (Minutes minutes: minutesList) {
            if (minutes.getDate().startsWith(filterMonth)) {
                target.add(minutes);
            }
        }
        return target;
    }
    @Transactional
    public Minutes watchDate(String date) {
        Minutes minutes = minutesRepository.findByDate(date);
        return minutes;
    }
    @Transactional
    public MinutesForm editMinutes(String date, MinutesForm dto) {
        //현재 입력한 date가 repository에 존재할 경우 edit. orElseThrow()
        Minutes minutes = minutesRepository.findByDate(date);
        if (dto.getTitle() != null) {
            minutes.updateTitle(dto.getTitle());
        }

        if (dto.getContent() != null) {
            minutes.updateContent(dto.getContent());
        }

        if (dto.getUserId() != minutes.getUserId()) {
            minutes.updateUserId(dto.getUserId());
        }
        MinutesForm dtotarget = minutes.toDto(minutes);
        return dtotarget;
    }

    @Transactional
    public void deleteMinutes(String date) {
        Minutes minutes = minutesRepository.findByDate(date);
        minutesRepository.delete(minutes);
    }

    //프로젝트+팀
    @Transactional
    public List<Members> watchMembers() {
        return membersRepository.findAll();
    }

    @Transactional
    public List<Projects> watchProjects() {
        return projectsRepository.findAll();
    }

    @Transactional
    public List<Teams> watchTeams() {
        return teamsRepository.findAll();
    }

    @Transactional
    public Projects getExistingProjects(String projectName) {
        Projects original = projectsRepository.findByProjectName(projectName);
        return original;
    }

    @Transactional
    public Projects createProjects(ProjectsForm dto) {
        Projects projects = projectsRepository.save(dto.toEntity(dto));
        return projects;
    }

    @Transactional
    public List<Projects> watchProjects(Long professor_id) {
        List<Projects> projectsList = projectsRepository.findByProfessorId(professor_id);
        return projectsList;
    }

    @Transactional
    public Teams createTeams(TeamsForm dto) {
        Teams teams = teamsRepository.save(dto.toEntity(dto));
        return teams;
    }


//    //팀멤버테이블에 추가 -> 미완성
    @Transactional
    public void createTeamsMembers(Long teamId, Long userId) {
        TeamMembers teamMembers = new TeamMembers();
        teamMembers.setTeamId(teamId);
        teamMembers.setUserId(userId);
        teamMembers.setContribution(0L);

        teamMembersRepository.save(teamMembers);
    }

    @Transactional
    public Teams countNums(Long teamId, Long projectId) {
        Projects projects = projectsRepository.findById(projectId).orElse(null);
        projects.setProjectNumber(projects.getProjectNumber() + 1);
        Teams teams = teamsRepository.findByTeamIdAndProjectId(teamId, projectId);
        teams.setTeamNumber(teams.getTeamNumber() + 1);
        return teams;
    }

    @Transactional
    public List<Teams> watchTeamsByPro(Long projectId) {
        List<Teams> teamsList = teamsRepository.findByProjectId(projectId);
        return teamsList;
    }

    @Transactional
    public List<Teams> watchTeamsByStu(Long id) {
        List<Long> teamsId = new ArrayList<>();
        List<Teams> teamsList = new ArrayList<>();

        List<TeamMembers> teamMembersList = teamMembersRepository.findByUserId(id);

        int cnt = teamMembersList.size();

        for (int i=0; i<cnt; i++) {
            TeamMembers teamMembers = teamMembersList.get(i); //객체 전체 선택
            teamsId.add(teamMembers.getTeamId());             //각 객체에서 teamId만 teamsId에 넣기
        }
        log.info("teamsId:"+ teamsId);

        int count = teamsId.size();

        for (int j=0; j<count; j++) {
            Teams teams = teamsRepository.findById(teamsId.get(j)).orElse(null);
            teamsList.add(teams);
        }

        log.info("teamsList:"+ teamsList);
        return teamsList;
    }

    @Transactional
    public boolean matchProfessorAndProject(Long professorId, Long projectId) {
        boolean check = false;
        Projects projects = projectsRepository.findByProfessorIdAndId(professorId, projectId);
        if (projects != null) {
            check = true;
        }
        return check;
    }

//    @Transactional
//    public TeamMembers addTeamMember(Long projectId ,Long teamId, Long userId) {
//        TeamMembers entity = new TeamMembers();
//        entity.setTeamId(teamId);
//        entity.setUserId(userId);
//        teamMembersRepository.save(entity);
//        return entity;
//    }

//    @Transactional
//    public List<Teams> getTeamsInProjects(Long projectId) {
//        Projects projects = projectsRepository.findByProjectId(projectId);
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
