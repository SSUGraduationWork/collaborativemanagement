package com.example.graduation.entity;

import com.example.graduation.dto.MinutesForm;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

//요청용
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Minutes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long minutes_id;

//    @Column
//    private Long userId;

    @Column
    private Long team_id;

    @Column
    private String date;
    @Column
    private String title;
    @Column
    private String content;

//    public void update(String title, String content, Long userId) {
//        this.title = title;
//        this.content = content;
////        this.userId = userId;
//    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
//        this.userId = userId;
    }

    public MinutesForm toDto(Minutes entity) {
        MinutesForm dto = new MinutesForm();
        dto.setId(entity.getMinutes_id());
//        dto.setUserId(entity.getUserId());
        dto.setTeamId(entity.getTeam_id());
        dto.setDate(entity.getDate());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        return dto;
    }
}

