package com.example.graduation.dto;

import com.example.graduation.entity.Minutes;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.SimpleTimeZone;


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
    public Minutes toEntity(MinutesForm dto) {
        Minutes entity = new Minutes();
        entity.setMinutesId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setTeamId(dto.getTeamId());
        entity.setDate(dto.getDate());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        return entity;
    }
}
