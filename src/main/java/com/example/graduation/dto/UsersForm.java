package com.example.graduation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UsersForm {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("password")
    private String password;
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
    @JsonProperty("datetime")
    private String datetime;
}
