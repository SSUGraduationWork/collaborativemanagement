package com.example.upload.Service;

import com.example.upload.Repository.AlarmRepository;
import com.example.upload.Repository.MemberRepository;
import com.example.upload.domain.Alarms;
import com.example.upload.domain.Boards;
import com.example.upload.domain.Members;
import com.example.upload.dto.response.AlarmDetailResponse;
import com.example.upload.dto.response.BoardDetailResponse;
import com.example.upload.dto.response.BoardResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AlarmService {
    private AlarmRepository alarmRepository;

    private MemberRepository memberRepository;
    //알람 자세히 보기
    public AlarmDetailResponse DetailAlarm(Long alarmId) {
        Alarms alarm = alarmRepository.findById(alarmId).orElse(null);
        if (alarm != null && !alarm.isSeen()) {
            // 알람이 존재하고, 아직 확인되지 않았으면 알람을 확인한 것으로 처리
            alarm.setSeen(true);
            alarmRepository.save(alarm);
        }
        return AlarmDetailResponse.from(alarm);
    }

    //게시글 리스트 처리리

    public List<AlarmDetailResponse> alarmList(Long memberId, Pageable pageable) {
        // memberId를 사용하여 해당 멤버의 알람 리스트를 가져옴
        Members member = memberRepository.findById(memberId).get();

                Page<Alarms> alarmsPage = alarmRepository.findByUsers(member, pageable);

        return alarmsPage.getContent().stream()
                .map(AlarmDetailResponse::from)
                .collect(Collectors.toList());
    }

}
