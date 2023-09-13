package com.example.authorizationserver.src.authorization.service;

import com.example.authorizationserver.src.entity.Board;
import com.example.authorizationserver.src.entity.Project;
import com.example.authorizationserver.src.entity.TeamMember;
import com.example.authorizationserver.src.repository.BoardRepository;
import com.example.authorizationserver.src.repository.ProjectRepository;
import com.example.authorizationserver.src.repository.TeamMemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final TeamMemberRepository teamMemberRepository;
    private final BoardRepository boardRepository;
    private final ProjectRepository projectRepository;

    //check permission for workspace
    public boolean checkViewPermission(Long teamId, Long userId, String role) {
        //학생인경우, 해당 팀에 속해 있는지 확인
        if (role.equals("student")){
            TeamMember member = teamMemberRepository.findByTeamIdAndUserId(teamId, userId)
                    .orElseThrow(IllegalAccessError::new);
            return true;
        //교수님인 경우, 해당 팀이 교수님의 Project에 속해 있는지 확인
        } else if(role.equals("professor")) {    // role == "professor"
            Project project = projectRepository.findByProfessorIdAndTeamId(userId, teamId)
                    .orElseThrow(IllegalAccessError::new);
            return true;

        } else {
            return false;
        }
    }
    //check permission for post
    public boolean checkPostPermission(Long teamId, Long userId, String role){
        //학생인 경우, 해당 팀에 속해 있는지 확인
        if (role.equals("student")) {
            TeamMember member = teamMemberRepository.findByTeamIdAndUserId(teamId, userId)
                    .orElseThrow(IllegalAccessError::new);
            return true;
        } else{  //교수님은 팀 내에 게시글에 대한 등록, 수정, 삭제 권한이 없음.
            return false;
        }
    }

    //check permission for modification
    public boolean checkModPermission(Long userId, Long boardId, String role){
        //학생인경우, 본인이 이 글의 작성자인지 확인
        if (role.equals("student")){

            Board board = boardRepository.findByUserIdAndBoardId(userId, boardId)
                    .orElseThrow(IllegalAccessError::new);
            return true;
        } else{ //교수님은 팀 내에 게시글에 대한 수정, 삭제 권한이 없음.
            return false;
        }
    }

}
