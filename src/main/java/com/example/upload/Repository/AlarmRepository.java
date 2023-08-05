package com.example.upload.Repository;

import com.example.upload.domain.Alarms;
import com.example.upload.domain.Boards;
import com.example.upload.domain.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarms, Long> {
    // 멤버별 알람 조회를 위한 메서드 추가
    Page<Alarms> findByUsers(Members member, Pageable pageable);
}

