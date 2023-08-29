package com.example.graduation.repository;

import com.example.graduation.entity.Projects;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ProjectsRepository extends CrudRepository<Projects, Long> {
    ArrayList<Projects> findAll();

    ArrayList<Projects> findByProfessorId(Long professorId);

    Projects findByProjectName(String projectName);

    Projects findByProfessorIdAndId(Long professorId, Long id);

//    @Query(SELECT * from (Members M inner join co_work.Projects P on M.id = P.professor_id)
//            INNER JOIN Teams T on P.professor_id = T.project_id)

}
