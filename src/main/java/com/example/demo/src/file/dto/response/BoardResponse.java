package com.example.demo.src.file.dto.response;


import com.example.demo.src.file.domain.Boards;
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

private String writerName;

private boolean feedbackYn;

private String pictureUrl;

private Long userId;
//사용자명과 작업명은 추후 추가해야함
//private String username;

//private String workname;
// 생성자


    @Builder
    public BoardResponse(Long boardId, String title, Long viewCount,
                         LocalDateTime createdTime, String workName,String writerName,
                         boolean feedbackYn,Long userId,String pictureUrl){
        this.boardId = boardId;
        this.title = title;
        this.viewCount=viewCount;
        this.createdTime=createdTime;
        this.workName=workName;
        this.writerName=writerName;
        this.feedbackYn=feedbackYn;
        this.userId=userId;
        this.pictureUrl=pictureUrl;
    }

    public static BoardResponse from(Boards boards) {
        return BoardResponse.builder()
                .boardId(boards.getId())
                .title(boards.getTitle())
                .viewCount(boards.getViewCnt())
                .createdTime(boards.getCreatedAt())
                .workName(boards.getWorks().getWorkName())
                .writerName(boards.getUsers().getStudentNumber()+" "+ boards.getUsers().getUserName())
                .userId(boards.getUsers().getId())
                .pictureUrl(boards.getUsers().getPictureUrl())
                .build();
    }
}
