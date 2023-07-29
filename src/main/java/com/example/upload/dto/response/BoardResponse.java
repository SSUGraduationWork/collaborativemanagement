package com.example.upload.dto.response;

import com.example.upload.domain.Board;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardWriteDto {
private Long boardId;
private String title;

private LocalDateTime createdTime;

private Long viewCount;

//사용자명과 작업명은 추후 추가해야함
//private String username;

//private String workname;

    @Builder
    public BoardWriteDto(Long boardId, String title, Long viewCount, LocalDateTime createdTime){
        this.boardId = boardId;
        this.title = title;
        this.viewCount=viewCount;
        this.createdTime=createdTime;
    }

    public static BoardWriteDto from(Board board) {
        return BoardWriteDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .viewCount(board.getViewCount())
                .createdTime(board.getCreatedDate())
                .build();
    }
}
