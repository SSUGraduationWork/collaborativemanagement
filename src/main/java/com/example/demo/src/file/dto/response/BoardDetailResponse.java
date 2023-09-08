package com.example.demo.src.file.dto.response;

import com.example.demo.src.file.domain.Boards;
import com.example.demo.src.file.domain.Files;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class BoardDetailResponse {

    private Long boardId;
    private String title;
    private String content;
    private List<String> fileDirs;
    private List<Long> fileId;
    private String workName;
    private Long writerId;
//사용자명과 작업명은 추후 추가해야함
//private String username;

//private String workname;

    @Builder
    public BoardDetailResponse(Long boardId, String title,String content,List<String> fileDirs,List<Long> fileId,String workName, Long writerId){
        this.boardId = boardId;
        this.title = title;
        this.content=content;
        this.fileDirs=fileDirs;
        this.fileId=fileId;
        this.workName=workName;
        this.writerId=writerId;
    }


    public static BoardDetailResponse from(Boards boards) {
        BoardDetailResponseBuilder responseBuilder = BoardDetailResponse.builder()
                .boardId(boards.getId())
                .title(boards.getTitle())
                .content(boards.getContent())
                .workName(boards.getWorks().getWorkName())
                .writerId(boards.getUsers().getId());
        List<String> fileDirs = new ArrayList<>();
        for (Files file : boards.getFileList()) {
            fileDirs.add(file.getFilepath());
        }

        List<Long> fileId = new ArrayList<>();
        for (Files file : boards.getFileList()) {
            fileId.add(file.getId());
        }

        responseBuilder.fileDirs(fileDirs);
        responseBuilder.fileId(fileId);
        return responseBuilder.build();
    }




}