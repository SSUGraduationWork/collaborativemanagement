package com.example.upload.domain;

import com.example.upload.FeedbackTimeEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity(name="Alarms")
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

    @Column(name = "seen", nullable = false)
    private boolean seen;

    @Column(name = "content", nullable = false, length = 255)
    private String content;

    @Column(name = "redirect_url", length = 100)
    private String redirectUrl;

    //연관관계 편의 메소드
    public void confirmMember(Members members){
        this.users=members;
        users.addAlarms(this);
    }



}