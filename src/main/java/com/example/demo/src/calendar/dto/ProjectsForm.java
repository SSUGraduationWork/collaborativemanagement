package com.example.demo.src.calendar.dto;

import com.example.demo.src.calendar.entity.Projects2;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ProjectsForm {
    @JsonProperty("projectId")
    private  Long projectId;

    @JsonProperty("professorId")
    private Long professorId;

    @JsonProperty("projectName")
    private String projectName;
    @JsonProperty("semester")
    private String semester;

    @JsonProperty("projectNumber")
    private Long projectNumber;

    // dto -> entity 연결
    public Projects2 toEntity(ProjectsForm dto) {
        Projects2 entity = new Projects2();
        entity.setProjectId(dto.getProjectId());
        entity.setProfessorId(dto.getProfessorId());
        entity.setProjectName(dto.getProjectName());
        entity.setSemester(dto.getSemester());
        entity.setProjectNumber(dto.getProjectNumber());
        return entity;
    }
}
