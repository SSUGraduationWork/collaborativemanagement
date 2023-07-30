package com.example.upload.Repository;

import com.example.upload.domain.Feedbacks;
import com.example.upload.domain.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Projects,Long> {
}
