package com.example.graduation.service;

import com.example.graduation.dto.MinutesForm;
import com.example.graduation.entity.Minutes;
import com.example.graduation.repository.MinutesRepository;
import jakarta.transaction.Transactional;
import jakarta.websocket.server.ServerEndpoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinutesService {

    private  final MinutesRepository minutesRepository;

    @Transactional
    public MinutesForm create(MinutesForm dto) {
        Minutes minutes= minutesRepository.save(dto.toEntity(dto));
        Minutes target = minutesRepository.findById(minutes.getMinutes_id()).orElse(null);
        MinutesForm targetToRecv = target.toDto(target);
        return targetToRecv;
    }

    @Transactional
    public List<Minutes> watchAll() {
        List<Minutes> target= minutesRepository.findAll();
        log.info(target.toString());
        return target;
    }

    @Transactional
    public List<Minutes> watchDates(String filterMonth) {
        List<Minutes> minutesList = minutesRepository.findByDateLike(filterMonth + "%");
        return minutesList;
    }

    @Transactional
    public Optional<Minutes> watch(String date) {
        Optional<Minutes> minutes = minutesRepository.findByDate(date);
        return minutes;
    }

    @Transactional
    public MinutesForm edit(String date, MinutesForm dto) {
        //현재 입력한 date가 repository에 존재할 경우 edit. orElseThrow()
        Minutes minutes = minutesRepository.findByDate(date).orElse(null);
        minutes.update(dto.getTitle(), dto.getContent());
        MinutesForm target = minutes.toDto(minutes);
        return target;
    }

    @Transactional
    public void delete(String date) {
        Minutes minutes = minutesRepository.findByDate(date).orElse(null);
        minutesRepository.delete(minutes);
    }
}
