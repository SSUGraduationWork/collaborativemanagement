package com.example.demo.src.calendar.repository;

import com.example.demo.src.calendar.entity.TeamMembers2;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamMembers2Repository extends CrudRepository<TeamMembers2, Long> {

     List<TeamMembers2> findByUserId(Long id);

     TeamMembers2 findByTeamIdAndUserId(Long teamId, Long userId);
}
