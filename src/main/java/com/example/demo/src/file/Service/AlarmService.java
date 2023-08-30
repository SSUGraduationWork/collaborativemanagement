package com.example.demo.src.file.Service;

import com.example.demo.src.file.Repository.AlarmRepository;
import com.example.demo.src.file.Repository.BoardRepository;
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

    private BoardRepository boardRepository;
    //알람 자세히 보기
    public AlarmDetailResponse DetailAlarm(Long alarmId) {
        Alarms alarms = alarmRepository.findById(alarmId).orElse(null);
        return AlarmDetailResponse.from(alarms);
    }

    //알람 리스트 처리리

    public List<AlarmDetailResponse> alarmList( Long memberId) {
        // memberId를 사용하여 해당 멤버의 알람 리스트를 가져옴
        Members member = membersRepository.findById(memberId).get();
        List<Alarms> alarms = alarmRepository.findByUsers(member);
        // 알람부분에서 피드백을 했는지 안했는지 확인해야, 프론트에서 피드백 여부에 따라 다르게 보여줄 수 있음.
        // 피드백을 거부했던 사람 한테는 거절,수락




        List<AlarmDetailResponse> alarmResponses = alarms.stream()
                .map(alarm -> {
                    List<FeedbackStatuses> feedbackStatusesList=alarm.getBoards().getFeedbackStatusList();
                    AlarmDetailResponse response = AlarmDetailResponse.from(alarm);
                    // feedbackYn 값을 해당 알람의 feedbackStatusesList를 순회하면서 설정
                    for (FeedbackStatuses feedbackStatuses : feedbackStatusesList) {
                        if (feedbackStatuses.getUsers().equals(member)) {
                            response.setFeedbackYn(feedbackStatuses.isFeedbackYn());
                            break; // feedbackStatusesList를 순회할 필요 없으므로 종료
                        }
                    }

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
