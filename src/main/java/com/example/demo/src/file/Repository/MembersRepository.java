package com.example.demo.src.file.Repository;

import com.example.demo.src.file.domain.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
public interface MembersRepository extends JpaRepository<Members,Long> {
    ArrayList<Members> findAll();

    @Query("SELECT m FROM Members m WHERE m.id = :memberId")
    Members findMemberById(@Param("memberId") Long memberId);

}
