package com.example.upload.domain;

import com.example.upload.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Data
public class Feedback extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "feedback_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String comment;
    //연관관계 편의 메소드
    public void confirmBoard(Board board){
        this.board=board;
        board.addFeedback(this);
    }

    // comment 필드의 getter 메서드 정의
    public String getComment() {
        return comment;
    }
}
