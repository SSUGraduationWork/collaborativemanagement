package com.example.authorizationserver.src.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class MemberDto {

    private Long id;
    private String email;
    private String name;
    private String role;
    private int studentNumber;
    private String university;
    private String department;
    private Timestamp createdAt;
    private String pictureUrl;

}
