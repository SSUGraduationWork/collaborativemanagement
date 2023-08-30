package com.example.demo.src.file.domain;

import com.example.demo.src.file.FeedbackTimeEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Table(name = "Alarms")
@Entity
@Data
@EqualsAndHashCode(callSuper = false)

public class Alarms extends FeedbackTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long alarmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Members users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Boards boards;

    @Column(name = "seen", nullable = false)
    private boolean seen;

    @Column(name = "content", nullable = false, length = 255)
    private String content;

    @Column(name = "redirect_url", length = 100)
    private String redirectUrl;


    @Column(name = "writer_picture_url")
    private String writerPictureUrl;

    @Column(name = "alarm_kind")
    private String alarmKind;

    @Column(name = "writer_id")
    private Long writerId;
    //연관관계 편의 메소드
    public void confirmMember(Members members){
        this.users=members;
        users.addAlarms(this);
    }
    //연관관계 편의 메소드
    public void confirmBoard(Boards boards){
        this.boards = boards;
        boards.addAlarms(this);
    }


}