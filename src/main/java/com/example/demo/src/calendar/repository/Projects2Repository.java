package com.example.demo.src.calendar.repository;

import com.example.demo.src.calendar.entity.Projects2;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface Projects2Repository extends CrudRepository<Projects2, Long> {
    ArrayList<Projects2> findAll();

    ArrayList<Projects2> findByProfessorId(Long professorId);

    Projects2 findByProjectName(String projectName);

    Projects2 findByProfessorIdAndId(Long professorId, Long id);

//    @Query(SELECT * from (Members2 M inner join co_work.Projects2 P on M.id = P.professor_id)
//            INNER JOIN Teams2 T on P.professor_id = T.project_id)

}
