package com.example.demo.src.calendar.dto;

import com.example.demo.src.calendar.entity.Minutes2;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MinutesForm {
    @JsonProperty("id")
    private  Long id;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("teamId")
    private Long teamId;
    @JsonProperty("date")
    private String date;
    @JsonProperty("title")
    private String title;
    @JsonProperty("content")
    private String content;


    // dto -> entity 연결
    public Minutes2 toEntity(MinutesForm dto) {
        Minutes2 entity = new Minutes2();
        entity.setMinutesId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setTeamId(dto.getTeamId());
        entity.setDate(dto.getDate());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        return entity;
    }
}
