package com.example.upload.dto.response;


import com.example.upload.domain.Boards;
import com.example.upload.domain.Feedbacks;
import lombok.Builder;
import lombok.Data;



@Data
public class FeedbackResponse {

    private Long feedbackId;

    private Long boardId;

    private String comment;

    public FeedbackResponse() {

    }

    public FeedbackResponse(String comment) {
        this.comment = comment;
    }

    @Builder
    public FeedbackResponse (Long feedbackId, Long boardId, String comment){
        this.boardId = boardId;
        this.feedbackId=feedbackId;
        this.comment=comment;
    }

    public static FeedbackResponse from(Feedbacks feedbacks,Boards boards) {
        return FeedbackResponse.builder()
                .feedbackId(feedbacks.getId())
                .boardId(boards.getId())
                .comment(feedbacks.getComment())
                .build();
    }
}



