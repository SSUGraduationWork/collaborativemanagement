package com.example.demo.src.calendar.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="Projects")
public class Projects2 {
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
//    private List<Teams2> teamsList;
//
//    public List<Teams2> getTeams() {
//        return teamsList;
//    }
}
