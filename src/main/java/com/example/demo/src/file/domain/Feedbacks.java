package com.example.demo.src.file.domain;


import com.example.demo.src.file.FeedbackTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="Feedbacks")
@Data
@EqualsAndHashCode(callSuper = false)
public class Feedbacks extends FeedbackTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Boards boards;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Members writers;


    @Column(name="mod_req")
    private boolean modReq;

    @Column(length = 500)
    private String comment;


    private boolean agree;

    //연관관계 편의 메소드
    public void confirmBoard(Boards boards){
        this.boards = boards;
        boards.addFeedbacks(this);
    }

    //연관관계 편의 메소드
    public void confirmMember(Members members){
        this.writers=members;
        writers.addFeedbacks(this);
    }



    // comment 필드의 getter 메서드 정의
    public String getComment() {
        return comment;
    }

    @Builder
    public Feedbacks(Long id, Boards boards, String comment){
        this.id=id;
        this.boards = boards;
        this.comment=comment;
        this.modReq=false;
        this.agree=false;
    }
}
