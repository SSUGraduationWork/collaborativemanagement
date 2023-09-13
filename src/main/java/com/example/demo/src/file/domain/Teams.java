package com.example.demo.src.file.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="Teams")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @JsonIgnore
    @OneToMany(mappedBy = "teams", cascade = ALL, orphanRemoval = true)
    private List<Boards> boardsList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "teams", cascade = ALL, orphanRemoval = true)
    private List<TeamMembers> teamList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "teams", cascade = ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FeedbackStatuses> feedbackStatusList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "teams", cascade = ALL, orphanRemoval = true)
    private List<Works> workList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "teams", cascade = ALL, orphanRemoval = true)
    private List<Workers> workerList = new ArrayList<>();

    //피드백 추가,연관관계 편의 메소드
    public void addBoards(Boards boards){
        //comment의 Post 설정은 comment에서 함
        boardsList.add(boards);
    }

    public void addTeams(TeamMembers teamMembers){
        //comment의 Post 설정은 comment에서 함
        teamList.add(teamMembers);
    }

    public void addFeedbackStatuses(FeedbackStatuses feedbackStatuses){
        //comment의 Post 설정은 comment에서 함
        feedbackStatusList.add(feedbackStatuses);
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
