package com.example.upload.Repository;

import com.example.upload.domain.Feedbacks;
import com.example.upload.domain.Teams;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Teams,Long> {
}
