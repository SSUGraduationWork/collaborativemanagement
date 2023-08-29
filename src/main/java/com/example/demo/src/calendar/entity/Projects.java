package com.example.demo.src.calendar.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Projects {
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "professor_id")
    private Long professorId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "semester")
    private String semester;

    @Column(name = "project_number")
    private Long projectNumber;

//    @OneToMany(mappedBy = "projects")
//    private List<Teams> teamsList;
//
//    public List<Teams> getTeams() {
//        return teamsList;
//    }
}
