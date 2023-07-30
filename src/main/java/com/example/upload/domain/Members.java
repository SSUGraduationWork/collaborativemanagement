package com.example.upload.domain;


import com.example.upload.FeedbackTimeEntity;
import com.example.upload.dto.response.BoardDetailResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="Members")
@Data
@EqualsAndHashCode(callSuper = false)
public class Members  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", length = 45)
    private String userId;

    @Column(length = 45)
    private String password;

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

    @OneToMany(mappedBy = "users", cascade = ALL, orphanRemoval = true)
    private List<Boards> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "writers", cascade = ALL, orphanRemoval = true)
    private List<Feedbacks> feedbackList = new ArrayList<>();

    @OneToMany(mappedBy = "professor", cascade = ALL, orphanRemoval = true)
    private List<Projects> projectList = new ArrayList<>();

    //피드백 추가,연관관계 편의 메소드
    public void addBoards(Boards boards){
        //comment의 Post 설정은 comment에서 함
        boardList.add(boards);
    }

    public void addFeedbacks(Feedbacks feedbacks){
        //comment의 Post 설정은 comment에서 함
        feedbackList.add(feedbacks);
    }
}
