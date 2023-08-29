package com.example.graduation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Members {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String user_email;
    @Column
    private String user_name;
    @Column
    private String role;

    @Column
    private Long student_number;

    @Column
    private String university;

    @Column
    private String department;

    @Column
    private String created_at;

    @Column
    private String picture_url;
}
