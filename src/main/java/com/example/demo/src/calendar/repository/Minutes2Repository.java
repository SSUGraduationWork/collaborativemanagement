package com.example.demo.src.calendar.repository;

import com.example.demo.src.calendar.entity.Minutes2;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

//extends 뒤에는 CrudRepository를 작성하여 여러 기능을 사용할 수 있도록 하고, <> 안의 파라미터는 사용할 entity와 그 대푯값(이 프로젝트에서는 id)의 타입 작성
public interface Minutes2Repository extends CrudRepository<Minutes2, Long> {
    @Override
    ArrayList<Minutes2> findAll();
    ArrayList<Minutes2> findByDateLike(String date);
    Minutes2 findByDate(String date);

    ArrayList<Minutes2> findByTeamId(Long teamId);
}
