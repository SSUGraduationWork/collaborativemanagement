package com.example.upload.Repository;

import com.example.upload.domain.Feedbacks;
import com.example.upload.domain.Works;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<Works,Long> {
}
