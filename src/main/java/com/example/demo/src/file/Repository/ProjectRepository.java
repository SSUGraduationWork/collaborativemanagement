package com.example.demo.src.file.Repository;

import com.example.demo.src.file.domain.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Projects,Long> {

}
