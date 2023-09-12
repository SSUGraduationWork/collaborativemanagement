package com.example.demo.src.file.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Entity
@Table(name = "Feedback_statuses")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FeedbackStatuses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_status_id")
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Members users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id",  nullable = false)
    private Boards boards;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Teams teams;

    @Column(name = "feedback_yn")
    private Integer feedbackYn;


    //연관관계 편의 메소드
    public void confirmBoard(Boards boards){
        this.boards = boards;
        boards.addFeedbackStatuses(this);
    }

    //연관관계 편의 메소드
    public void confirmMember (Members members){
        this.users=members;
        users.addFeedbackStatuses(this);
    }

    //연관관계 편의 메소드
    public void confirmTeam (Teams teams){
        this.teams=teams;
        teams.addFeedbackStatuses(this);
    }
    //게시판에 대한 피드백 승인 메소드
    public void feedbackAgree(){
        this.feedbackYn = 1;
    }

    //게시판에 대한 피드백 거절 메소드
    public void feedbackDeny(){
        this.feedbackYn = 2;
    }

    @Builder
    public FeedbackStatuses(Members members ){
        this.boards = boards;
        this.feedbackYn=0;
        this.users=members;
    }

}