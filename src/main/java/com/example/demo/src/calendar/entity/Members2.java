package com.example.demo.src.calendar.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="Members")
public class Members2 {
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
