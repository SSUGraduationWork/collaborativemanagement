package com.example.upload.dto.response;


import com.example.upload.domain.Boards;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardResponse {
private Long boardId;
private String title;

private LocalDateTime createdTime;

private Long viewCount;

private String workName;
//사용자명과 작업명은 추후 추가해야함
//private String username;

//private String workname;

    @Builder
    public BoardResponse(Long boardId, String title, Long viewCount, LocalDateTime createdTime, String workName){
        this.boardId = boardId;
        this.title = title;
        this.viewCount=viewCount;
        this.createdTime=createdTime;
        this.workName=workName;
    }

    public static BoardResponse from(Boards boards) {
        return BoardResponse.builder()
                .boardId(boards.getId())
                .title(boards.getTitle())
                .viewCount(boards.getViewCnt())
                .createdTime(boards.getCreatedAt())
                .workName(boards.getWorks().getWorkName())
                .build();
    }
}
