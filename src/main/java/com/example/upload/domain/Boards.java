package com.example.upload.domain;

import com.example.upload.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="Boards")
@Data
@EqualsAndHashCode(callSuper = false)
public class Boards extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Teams teams;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id")
    private Works works;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Members users;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(length = 3000)
    private String content;

    //파일 정보 저장
    @Column(length = 100)
    private String filename;

    @Column(length = 100)
    private String filepath;

    @Column(name = "view_cnt")
    private Long viewCnt;     //조회수

    @Column(name = "del_yn")
    private boolean delYn;

    //== 게시글을 삭제하면 달려있는 댓글 모두 삭제 ==//
    @OneToMany(mappedBy = "boards", cascade = ALL, orphanRemoval = true)
    private List<Feedbacks> feedbackList = new ArrayList<>();


    public Boards updateViewCount(Long viewCount){
        this.viewCnt = viewCount+1;
        return this;
    }

    //피드백 추가,연관관계 편의 메소드
    public void addFeedbacks(Feedbacks feedbacks){
        //comment의 Post 설정은 comment에서 함
        feedbackList.add(feedbacks);
    }
    //연관관계 편의 메소드
    public void confirmMember(Members members){
        this.users=members;
        members.addBoards(this);
    }

    //연관관계 편의 메소드
    public void confirmTeam(Teams teams){
        this.teams=teams;
        teams.addBoards(this);
    }

    //연관관계 편의 메소드
    public void confirmWork(Works works){
        this.works=works;
        works.addBoards(this);
    }
    @Builder
    public Boards(String title, String content,String filename, String filepath){
        this.title = title;
        this.content = content;
        this.viewCnt = 0L;
        this.filename=filename;
        this.filepath=filepath;
        this.delYn=false;
    }



}
