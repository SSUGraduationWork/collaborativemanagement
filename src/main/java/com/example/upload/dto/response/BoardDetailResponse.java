package com.example.upload.dto.response;

import com.example.upload.domain.Boards;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BoardDetailResponse {

    private Long boardId;
    private String title;


//사용자명과 작업명은 추후 추가해야함
//private String username;

//private String workname;

    @Builder
    public BoardDetailResponse(Long boardId, String title, Long viewCount, LocalDateTime createdTime){
        this.boardId = boardId;
        this.title = title;
    }

    public static BoardDetailResponse from(Boards boards) {
        return BoardDetailResponse.builder()
                .boardId(boards.getId())
                .title(boards.getTitle())
                .build();
    }
}