package com.example.demo.src.file.Repository;


import com.example.demo.src.file.domain.Boards;
import com.example.demo.src.file.domain.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Boards,Long>
{
    List<Boards> findByTeams(Teams team);
    Boards findByIdAndTeamsId(Long boardId, Long teamId);
    List<Boards> findAllByTeamsId(Long teamId);
}
