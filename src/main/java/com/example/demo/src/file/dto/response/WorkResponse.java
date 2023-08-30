package com.example.demo.src.file.dto.response;

import com.example.demo.src.file.domain.Works;
import lombok.Builder;
import lombok.Data;

@Data
public class WorkResponse {
    private Long workId;
    private String workName;

    @Builder
    public WorkResponse(Long workId, String workName){
        this.workId=workId;
        this.workName=workName;
    }

    public static WorkResponse from(Works works) {
        return WorkResponse.builder()
                .workId(works.getId())
                .workName(works.getWorkName())
                .build();
    }
}
