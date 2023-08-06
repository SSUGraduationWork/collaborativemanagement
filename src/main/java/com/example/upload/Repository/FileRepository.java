package com.example.upload.Repository;

import com.example.upload.domain.Boards;
import com.example.upload.domain.FeedbackStatuses;
import com.example.upload.domain.Files;
import com.example.upload.domain.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface FileRepository extends JpaRepository<Files, Long> {

}