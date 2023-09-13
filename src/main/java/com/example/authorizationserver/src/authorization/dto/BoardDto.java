package com.example.authorizationserver.src.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
public class BoardDto {

    private Long id;
    private Long teamId;
    private Long workId;
    private Long userId;
    private String title;
    private String content;
    private Integer feedbackYN;
    private Integer viewCnt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String delYN;
}
