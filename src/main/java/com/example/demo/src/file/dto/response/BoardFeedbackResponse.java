package com.example.demo.src.file.dto.response;

import com.example.demo.src.file.domain.FeedbackStatuses;
import com.example.demo.src.file.domain.Feedbacks;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BoardFeedbackResponse  {

    private Long feedbackId;


    private String comment;

    private LocalDateTime createdAt;
    private Integer studentNumber;
    private String userName;

    private String pictureUrl;

    private boolean modReq;
    public BoardFeedbackResponse() {

    }

    public BoardFeedbackResponse(String comment) {
        this.comment = comment;
    }

    @Builder
    public BoardFeedbackResponse (Long feedbackId, String comment,LocalDateTime createdAt,
                             Integer studentNumber,String userName,String pictureUrl, boolean modReq){

        this.feedbackId=feedbackId;
        this.comment=comment;
        this.createdAt=createdAt;
        this.studentNumber=studentNumber;
        this.userName=userName;
        this.pictureUrl=pictureUrl;
        this.modReq=modReq;
    }

    public static BoardFeedbackResponse from(Feedbacks feedbacks, FeedbackStatuses feedbackStatuses) {
        return BoardFeedbackResponse.builder()
                .feedbackId(feedbacks.getId())
                .comment(feedbacks.getComment())
                .createdAt(feedbacks.getCreatedAt())
                .studentNumber(feedbacks.getWriters().getStudentNumber())
                .userName(feedbacks.getWriters().getUserName())
                .pictureUrl(feedbacks.getWriters().getPictureUrl())
                .modReq(feedbackStatuses.isFeedbackYn())
                .build();
    }
}

