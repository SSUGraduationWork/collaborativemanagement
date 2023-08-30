package com.example.demo.src.calendar.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="Teams")
public class Teams2 {
    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "team_number")
    private Long teamNumber;


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "project_id")
//    private Projects2 projects;
//
//
//    public void setProjects(Projects2 projects) {
//        this.projects = projects;
//    }
}
