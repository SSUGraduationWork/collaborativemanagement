package com.example.upload.Repository;


import com.example.upload.domain.Feedbacks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackRepository extends JpaRepository<Feedbacks,Long>
{
    List<Feedbacks> findAllByBoardsId(Long boardId);
}