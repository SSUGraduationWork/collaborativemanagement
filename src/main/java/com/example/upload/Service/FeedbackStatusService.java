package com.example.upload.Service;

import com.example.upload.Repository.FeedbackStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackStatusService {

    private final FeedbackStatusRepository feedbackStatusRepository;

    @Autowired
    public FeedbackStatusService(FeedbackStatusRepository feedbackStatusRepository) {
        this.feedbackStatusRepository = feedbackStatusRepository;
    }


}