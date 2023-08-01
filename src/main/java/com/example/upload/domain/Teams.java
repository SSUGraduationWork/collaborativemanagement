package com.example.upload.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="Teams")
@Data
@EqualsAndHashCode(callSuper = false)
public class Teams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Projects project;

    @Column(name = "team_name",  length = 10)
    private String teamName;

    @Column(name = "team_number")
    private Integer teamNumber;


    @OneToMany(mappedBy = "teams", cascade = ALL, orphanRemoval = true)
    private List<Boards> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "teams", cascade = ALL, orphanRemoval = true)
    private List<TeamMembers> teamList = new ArrayList<>();

    //피드백 추가,연관관계 편의 메소드
    public void addBoards(Boards boards){
        //comment의 Post 설정은 comment에서 함
        boardList.add(boards);
    }

    public void addTeams(TeamMembers teamMembers){
        //comment의 Post 설정은 comment에서 함
        teamList.add(teamMembers);
    }

    // 해당 팀에 속한 모든 멤버 가져오기
    public List<Members> getAllMembers() {
        List<Members> members = new ArrayList<>();
        for (TeamMembers teamMember : teamList) {
            members.add(teamMember.getUsers());
        }
        return members;
    }

/*
    // 해당 팀에 속한 모든 멤버 가져오기
    public List<Members> getMembers() {
        List<Members> members = new ArrayList<>();
        for (Members member : membersList) {
            members.add(member);
        }
        return members;
    }


 */
}
