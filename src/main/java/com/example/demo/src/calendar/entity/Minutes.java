package com.example.demo.src.calendar.entity;

import com.example.demo.src.calendar.dto.MinutesForm;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "minutes_id")
    private Long minutesId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "team_id")
    private Long teamId;

    @Column
    private String date;
    @Column
    private String title;
    @Column
    private String content;

    public void updateTitle(String title) {
        this.title = title;
    }
    public void updateContent(String content) {
        this.content = content;
    }

    public void updateUserId(Long user_id) {
        this.userId = user_id;
    }

    public MinutesForm toDto(Minutes entity) {
        MinutesForm dto = new MinutesForm();
        dto.setId(entity.getMinutesId());
        dto.setUserId(entity.getUserId());
        dto.setTeamId(entity.getTeamId());
        dto.setDate(entity.getDate());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        return dto;
    }

}

