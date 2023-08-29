package com.example.graduation.repository;

import com.example.graduation.entity.Teams;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface TeamsRepository extends CrudRepository<Teams, Long> {
    ArrayList<Teams> findAll();

    ArrayList<Teams> findByProjectId(Long projectId);
    Teams findByTeamIdAndProjectId(Long teamId, Long projectId);

    Teams findByTeamName(String teamName);
}
