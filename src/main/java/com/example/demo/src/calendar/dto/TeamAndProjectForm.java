package com.example.demo.src.calendar.dto;

import com.example.demo.src.calendar.entity.Teams2;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class TeamAndProjectForm {
    @JsonProperty("teamId")
    private  Long teamId;

    @JsonProperty("projectId")
    private Long projectId;

    @JsonProperty("teamName")
    private String teamName;
    @JsonProperty("teamNumber")
    private Long teamNumber;

    @JsonProperty("semester")
    private String semester;

    @JsonProperty("projectName")
    private String projectName;

    public Teams2 toEntity(TeamsForm dto) {
        Teams2 entity = new Teams2();
        entity.setTeamId(dto.getTeamId());
        entity.setProjectId(dto.getProjectId());
        entity.setTeamName(dto.getTeamName());
        entity.setTeamNumber(dto.getTeamNumber());
        return entity;
    }

}
