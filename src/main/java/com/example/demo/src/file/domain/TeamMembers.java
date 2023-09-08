package com.example.demo.src.file.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Table(name="Team_members")
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class TeamMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Teams teams;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Members users;

    @Column(name = "contribution")
    private float contribution;

    // 생성자를 추가하여 contribution을 0으로 초기화
    public TeamMembers() {
        this.contribution = 0;
    }

    //연관관계 편의 메소드
    public void confirmTeam(Teams teams){
        this.teams=teams;
        teams.addTeams(this);
    }

    //연관관계 편의 메소드
    public void confirmMember(Members members){
        this.users=members;
        users.addMembers(this);
    }

    public TeamMembers addContribution(float num){
        this.contribution+=num;
        return this;
    }

}