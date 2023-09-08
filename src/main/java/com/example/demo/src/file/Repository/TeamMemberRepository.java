package com.example.demo.src.file.Repository;

import com.example.demo.src.file.domain.FeedbackStatuses;
import com.example.demo.src.file.domain.TeamMembers;
import com.example.demo.src.file.domain.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMembers,Long> {
    TeamMembers findByTeamsIdAndUsersId(Long TeamId, Long memberId);
}
