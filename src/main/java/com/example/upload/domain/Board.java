package com.example.upload.domain;

import com.example.upload.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
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
    private List<Feedback> feedbackList = new ArrayList<>();

    public Board updateViewCount(Long viewCount){
        this.viewCount = viewCount+1;
        return this;
    }

    //피드백 추가,연관관계 편의 메소드
    public void addFeedback(Feedback feedback){
        //comment의 Post 설정은 comment에서 함
        feedbackList.add(feedback);
    }

    @Builder
    public Board(String title, String content){
        this.title = title;
        this.content = content;
        this.viewCount = 0L;
    }
}
