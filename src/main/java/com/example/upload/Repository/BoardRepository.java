package com.example.upload.Repository;


import com.example.upload.domain.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Boards,Long>
{
}