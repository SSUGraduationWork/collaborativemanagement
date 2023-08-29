package com.example.graduation.service;

import com.example.graduation.dto.MinutesForm;
import com.example.graduation.entity.Minutes;
import com.example.graduation.repository.MinutesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.quartz.QuartzTransactionManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinutesService {

    private  final MinutesRepository minutesRepository;



    @Transactional
    public Minutes getExistingMinutes(String date) {
        Minutes original = minutesRepository.findByDate(date);
        return original;
    }

    @Transactional
    public Minutes create(MinutesForm dto) {

        Minutes minutes= minutesRepository.save(dto.toEntity(dto));
        Minutes target = minutesRepository.findById(minutes.getMinutesId()).orElse(null);
        return target;
    }

    @Transactional
    public List<Minutes> watchAll(Long teamId) {
        List<Minutes> minutesList= minutesRepository.findByTeamId(teamId);
        return minutesList;
    }

    @Transactional
    public List<Minutes> watchDates(Long teamId,String filterMonth) {
        List<Minutes> target = new ArrayList<>();
        List<Minutes> minutesList = minutesRepository.findByTeamId(teamId);

        for (Minutes minutes: minutesList) {
            if (minutes.getDate().startsWith(filterMonth)) {
                target.add(minutes);
            }
        }
        return target;
    }

    @Transactional
    public Minutes watchDate(String date) {
        Minutes minutes = minutesRepository.findByDate(date);
        return minutes;
    }

    //수정할 것:dto로 아래 3개중하나만 바뀔 경우 나머지는 그대로 유지하는 것 구현하기
    @Transactional
    public MinutesForm editMinutes(String date, MinutesForm dto) {
        //현재 입력한 date가 repository에 존재할 경우 edit. orElseThrow()
        Minutes minutes = minutesRepository.findByDate(date);
        if (dto.getTitle() != null) {
            minutes.updateTitle(dto.getTitle());
        }

        if (dto.getContent() != null) {
            minutes.updateContent(dto.getContent());
        }

        if (dto.getUserId() != minutes.getUserId()) {
            minutes.updateUserId(dto.getUserId());
        }
        MinutesForm dtotarget = minutes.toDto(minutes);
        return dtotarget;
    }

    @Transactional
    public void deleteMinutes(String date) {
        Minutes minutes = minutesRepository.findByDate(date);
        minutesRepository.delete(minutes);
    }
}
