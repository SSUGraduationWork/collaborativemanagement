package com.example.upload.dto.response;

import com.example.upload.domain.Alarms;
import com.example.upload.domain.Boards;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmDetailResponse {
    private Long alarmId;

    private boolean seen;

    private String content;

    private String redirectUrl;

    private LocalDateTime createdTime;

    @Builder
    public AlarmDetailResponse(Long alarmId, boolean seen,String content, String redirectUrl,LocalDateTime createdTime){
        this.alarmId = alarmId;
        this.seen = seen;
        this.content=content;
        this.redirectUrl=redirectUrl;
        this.createdTime=createdTime;
    }

    public static AlarmDetailResponse from(Alarms alarms) {
        return AlarmDetailResponse.builder()
                .alarmId(alarms.getAlarmId())
                .seen(alarms.isSeen())
                .content(alarms.getContent())
                .redirectUrl(alarms.getRedirectUrl())
                .createdTime(alarms.getCreatedAt())
                .build();
    }


}



