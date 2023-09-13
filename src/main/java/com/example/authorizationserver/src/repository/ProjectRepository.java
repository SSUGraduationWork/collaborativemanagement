package com.example.authorizationserver.src.repository;

import com.example.authorizationserver.src.entity.Project;
import com.example.authorizationserver.src.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p LEFT JOIN Team t ON p.projectId = t.project.projectId WHERE p.professorId = :professorId AND t.teamId=:teamId")
    Optional<Project> findByProfessorIdAndTeamId(Long professorId, Long teamId);
}
