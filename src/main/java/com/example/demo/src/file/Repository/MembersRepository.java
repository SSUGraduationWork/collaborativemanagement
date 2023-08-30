package com.example.demo.src.file.Repository;

import com.example.demo.src.file.domain.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface MembersRepository extends JpaRepository<Members,Long> {
    Optional<Members> findByUserEmail(String email);
    ArrayList<Members> findAll();
}
