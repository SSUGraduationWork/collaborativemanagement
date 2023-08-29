package com.example.graduation.dto;

import com.example.graduation.entity.Minutes;
import com.example.graduation.entity.Projects;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ProjectsForm {
    @JsonProperty("id")
    private  Long id;

    @JsonProperty("professorId")
    private Long professorId;

    @JsonProperty("projectName")
    private String projectName;
    @JsonProperty("semester")
    private String semester;

    @JsonProperty("projectNumber")
    private Long projectNumber;

    // dto -> entity 연결
    public Projects toEntity(ProjectsForm dto) {
        Projects entity = new Projects();
        entity.setId(dto.getId());
        entity.setProfessorId(dto.getProfessorId());
        entity.setProjectName(dto.getProjectName());
        entity.setSemester(dto.getSemester());
        entity.setProjectNumber(dto.getProjectNumber());
        return entity;
    }
}
