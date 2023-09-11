package com.example.demo.src.file.Repository;

import com.example.demo.src.file.domain.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Teams,Long> {
    @Query("SELECT t FROM Teams t WHERE t.id = :teamId")
    Teams findTeamById(@Param("teamId") Long teamId);
}
