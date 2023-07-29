package com.example.upload.Repository;

import com.example.upload.domain.Board;
import com.example.upload.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long>
{
    List<Feedback> findAllByBoardId(Long boardId);
}