package com.example.demo.src.file.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="Projects")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Projects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Members professor;

    @Column(name = "project_name", length = 20)
    private String projectName;

    @Column(name = "semester",  length = 10)
    private String semester;

    @Column(name = "project_number")
    private Long projectNumber;

}
