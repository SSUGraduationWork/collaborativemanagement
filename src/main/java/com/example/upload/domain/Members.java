/*package com.example.upload.domain;


import com.example.upload.FeedbackTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Data
public class Member  {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String memberId;

    private String password;

    //멤버 이름과 학번을 합쳐?
    private String memberName;

    private Role role;

    private  String studentNumber;
}
*/