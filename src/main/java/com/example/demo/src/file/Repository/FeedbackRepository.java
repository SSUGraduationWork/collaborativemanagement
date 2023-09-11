package com.example.demo.src.file.Repository;


import com.example.demo.src.file.domain.Feedbacks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackRepository extends JpaRepository<Feedbacks,Long>
{


    @Query("SELECT f FROM Feedbacks f WHERE f.boards.id = :boardId")
    List<Feedbacks> findAllByBoardsId(@Param("boardId") Long board);
}