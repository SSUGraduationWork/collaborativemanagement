package com.example.upload.dto.response;

import com.example.upload.domain.Boards;

import com.example.upload.domain.Files;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
public class BoardDetailResponse {

    private Long boardId;
    private String title;
    private String content;
    private List<String> fileDirs;
//사용자명과 작업명은 추후 추가해야함
//private String username;

//private String workname;

    @Builder
    public BoardDetailResponse(Long boardId, String title,String content,List<String> fileDirs){
        this.boardId = boardId;
        this.title = title;
        this.content=content;
        this.fileDirs=fileDirs;
    }


    public static BoardDetailResponse from(Boards boards) {
        BoardDetailResponse.BoardDetailResponseBuilder responseBuilder = BoardDetailResponse.builder()
                .boardId(boards.getId())
                .title(boards.getTitle())
                .content(boards.getContent());

        List<String> fileDirs = new ArrayList<>();
        for (Files file : boards.getFileList()) {
            fileDirs.add(file.getFilepath());
        }

        responseBuilder.fileDirs(fileDirs);

        return responseBuilder.build();
    }




}