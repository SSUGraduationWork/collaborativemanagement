package com.example.demo.src.file.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class FeedbackStatusResponse {
    private Integer finishedCnt;

    private  Integer todoCnt;

    @Builder
    public FeedbackStatusResponse (Integer finishedCnt, Integer todoCnt){
        this.finishedCnt=finishedCnt;
        this.todoCnt=todoCnt;
    }

}
