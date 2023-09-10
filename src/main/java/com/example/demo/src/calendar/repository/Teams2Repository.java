package com.example.demo.src.calendar.repository;

import com.example.demo.src.calendar.entity.Teams2;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface Teams2Repository extends CrudRepository<Teams2, Long> {
    ArrayList<Teams2> findAll();

    ArrayList<Teams2> findByProjectId(Long projectId);
    Teams2 findByTeamIdAndProjectId(Long teamId, Long projectId);

    Teams2 findByTeamName(String teamName);

    Teams2 findByTeamId(Long teamId);
}
