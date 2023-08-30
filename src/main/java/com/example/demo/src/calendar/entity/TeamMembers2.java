package com.example.demo.src.calendar.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@DynamicInsert
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name = "Team_members")
public class TeamMembers2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_member_id")
    private Long teamMemberId;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "contribution")
    private Long contribution;


}
