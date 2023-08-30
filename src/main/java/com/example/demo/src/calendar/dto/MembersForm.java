package com.example.demo.src.calendar.dto;

import com.example.demo.src.calendar.entity.Members2;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MembersForm {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("role")
    private String role;
    @JsonProperty("studentNumber")
    private Long studentNumber;
    @JsonProperty("university")
    private String university;

    @JsonProperty("department")
    private String department;

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("pictureUrl")
    private String pictureUrl;

    public Members2 toEntity(MembersForm dto) {
        Members2 entity = new Members2();
        entity.setUser_email(dto.getUserId());
        entity.setUser_name(dto.getUserName());
        entity.setRole(dto.getRole());
        entity.setStudent_number(dto.getStudentNumber());
        entity.setUniversity(dto.getUniversity());
        entity.setDepartment(dto.getDepartment());
        entity.setCreated_at(dto.getCreatedAt());
        entity.setPicture_url(dto.getPictureUrl());
        return entity;
    }

}
