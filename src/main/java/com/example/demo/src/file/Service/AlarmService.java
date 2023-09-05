package com.example.demo.src.file.Service;

import com.example.demo.src.file.Repository.AlarmRepository;
import com.example.demo.src.file.Repository.BoardRepository;
import com.example.demo.src.file.Repository.FeedbackStatusRepository;
import com.example.demo.src.file.Repository.MembersRepository;
import com.example.demo.src.file.domain.Alarms;
import com.example.demo.src.file.domain.FeedbackStatuses;
import com.example.demo.src.file.domain.Members;
import com.example.demo.src.file.dto.response.AlarmDetailResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AlarmService {
    private AlarmRepository alarmRepository;

    private MembersRepository membersRepository;

    private FeedbackStatusRepository feedbackStatusRepository;
    //알람 자세히 보기
    public AlarmDetailResponse DetailAlarm(Long alarmId) {
        Alarms alarms = alarmRepository.findById(alarmId).orElse(null);
        return AlarmDetailResponse.from(alarms);
    }

    //알람 리스트 처리리

    public List<AlarmDetailResponse> alarmList( Long memberId) {
        // memberId를 사용하여 해당 멤버의 알람 리스트를 가져옴

        List<Alarms> alarms = alarmRepository.findByUsersId(memberId);
        // 알람부분에서 피드백을 했는지 안했는지 확인해야, 프론트에서 피드백 여부에 따라 다르게 보여줄 수 있음.
        // 피드백을 거부했던 사람 한테는 거절,수락
        System.out.println("alarmList개수"+alarms.size());
        List<AlarmDetailResponse> alarmResponses = alarms.stream()
                .map(alarm -> {

                    FeedbackStatuses feedbackStatuses=feedbackStatusRepository.findByBoardsIdAndUsersId(alarm.getBoards().getId(),memberId);
                    AlarmDetailResponse response = AlarmDetailResponse.from(alarm);
                    response.setFeedbackYn(feedbackStatuses.isFeedbackYn());
                    return response;
                })
                .collect(Collectors.toList());

        return alarmResponses;
    }



    public void updateSeenStatus(Long alarmId) {
        Alarms alarms = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new EntityNotFoundException("Alarms not found with ID: " + alarmId));

        alarms.setSeen(true);
        alarmRepository.save(alarms);
    }


}
