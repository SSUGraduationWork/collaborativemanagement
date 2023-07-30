package com.example.upload.dto.request;

import lombok.Data;

@Data
public class FeedbackRequest {
    private Long feedbackId;
    private String comment;
}
