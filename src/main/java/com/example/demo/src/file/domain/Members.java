package com.example.demo.src.file.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Entity
@Table(name="Members")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Members  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

   // @Column(name = "user_id", length = 45)
   // private String userId;


    @Column(name = "user_name", length = 10)
    private String userName;

    @Column( length = 10)
    private String role;

    @Column(name = "student_number")
    private Integer studentNumber;

    @Column(name = "university",length = 10)
    private String university;

    @Column(length = 20)
    private String department;

    @Column(name="picture_url",length = 100)
    private String pictureUrl;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @OneToMany(mappedBy = "users", cascade = ALL, orphanRemoval = true)
    private List<Boards> boardsList = new ArrayList<>();

    @OneToMany(mappedBy = "writers", cascade = ALL, orphanRemoval = true)
    private List<Feedbacks> feedbacksList = new ArrayList<>();

    @OneToMany(mappedBy = "professor", cascade = ALL, orphanRemoval = true)
    private List<Projects> projectList = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = ALL, orphanRemoval = true)
    private List<FeedbackStatuses> feedbackStatusList = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = ALL, orphanRemoval = true)
    private List<TeamMembers> memberList = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = ALL, orphanRemoval = true)
    private List<Alarms> alarmsList = new ArrayList<>();

    public void addFeedbackStatuses(FeedbackStatuses feedbackStatuses){
        //comment의 Post 설정은 comment에서 함
        feedbackStatusList.add(feedbackStatuses);
    }

    //피드백 추가,연관관계 편의 메소드
    public void addBoards(Boards boards){
        //comment의 Post 설정은 comment에서 함
        boardsList.add(boards);
    }

    public void addFeedbacks(Feedbacks feedbacks){
        //comment의 Post 설정은 comment에서 함
        feedbacksList.add(feedbacks);
    }


    public void addMembers(TeamMembers teamMembers){
        //comment의 Post 설정은 comment에서 함
        memberList.add(teamMembers);
    }

    public void addAlarms(Alarms alarms){
        //comment의 Post 설정은 comment에서 함
        alarmsList.add(alarms);
    }

    @Builder
    public Members(String email, String name, String role, int studentNumber, String university, String department, String pictureUrl){
        this.userEmail = email;
        this.userName = name;
        this.role = role;
        this.studentNumber = studentNumber;
        this.university = university;
        this.department = department;
        this.pictureUrl = pictureUrl;
    }
}
