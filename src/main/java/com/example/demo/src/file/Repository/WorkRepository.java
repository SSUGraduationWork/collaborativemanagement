package com.example.demo.src.file.Repository;

import com.example.demo.src.file.domain.Teams;
import com.example.demo.src.file.domain.Works;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WorkRepository extends JpaRepository<Works,Long> {
    List<Works> findByTeams(Teams teams);
}
