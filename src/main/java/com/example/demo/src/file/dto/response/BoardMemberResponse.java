package com.example.demo.src.file.dto.response;

import com.example.demo.src.file.domain.Members;
import com.example.demo.src.file.domain.Works;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BoardMemberResponse {


    private Long userId;
    private String writerName;
    private String pictureUrl;
    private Integer studentNumber;
    @Builder
    public BoardMemberResponse (Long userId, String writerName,String pictureUrl,Integer studentNumber){
        this.userId=userId;
        this.writerName=writerName;
        this.pictureUrl=pictureUrl;
        this.studentNumber=studentNumber;
    }

    public static BoardMemberResponse  from(Members members) {
        return BoardMemberResponse .builder()
                .userId(members.getId())
                .writerName(members.getUserName())
                .pictureUrl(members.getPictureUrl())
                .studentNumber(members.getStudentNumber())
                .build();
    }

}