package com.example.upload.domain;

import com.example.upload.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Data
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @NotNull(message = "제목은 필수입니다.")
    private String title;
    private String content;

    //파일 정보 저장
    private String filename;
    private String filepath;


    private Long viewCount;     //조회수


    //== 게시글을 삭제하면 달려있는 댓글 모두 삭제 ==//
    @OneToMany(mappedBy = "board", cascade = ALL, orphanRemoval = true)
    private List<Feedbacks> feedbackList = new ArrayList<>();


    public Board updateViewCount(Long viewCount){
        this.viewCount = viewCount+1;
        return this;
    }

    //피드백 추가,연관관계 편의 메소드
    public void addFeedbacks(Feedbacks feedbacks){
        //comment의 Post 설정은 comment에서 함
        feedbackList.add(feedbacks);
    }

    @Builder
    public Board(String title, String content,String filename, String filepath){
        this.title = title;
        this.content = content;
        this.viewCount = 0L;
        this.filename=filename;
        this.filepath=filepath;
    }

}
