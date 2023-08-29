package com.example.demo.src.calendar.repository;

import com.example.demo.src.calendar.entity.Teams;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface TeamsRepository extends CrudRepository<Teams, Long> {
    ArrayList<Teams> findAll();

    ArrayList<Teams> findByProjectId(Long projectId);
    Teams findByTeamIdAndProjectId(Long teamId, Long projectId);

    Teams findByTeamName(String teamName);
}
