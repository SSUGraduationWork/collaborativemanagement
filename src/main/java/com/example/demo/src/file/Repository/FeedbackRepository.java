package com.example.demo.src.file.Repository;


import com.example.demo.src.file.domain.Feedbacks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackRepository extends JpaRepository<Feedbacks,Long>
{

    List<Feedbacks> findAllByBoardsId(Long boardId);
}