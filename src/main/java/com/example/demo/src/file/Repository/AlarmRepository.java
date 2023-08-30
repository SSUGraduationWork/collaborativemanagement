package com.example.demo.src.file.Repository;

import com.example.demo.src.file.domain.Alarms;
import com.example.demo.src.file.domain.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarms, Long> {
    // 멤버별 알람 조회를 위한 메서드 추가
    List<Alarms> findByUsers(Members member);

}

