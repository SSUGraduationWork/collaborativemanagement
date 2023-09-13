package com.example.demo.src.file.Repository;


import com.example.demo.src.file.domain.Members;
import com.example.demo.src.file.domain.TeamMembers;
import com.example.demo.src.file.domain.Teams;
import com.example.demo.src.file.dto.response.FeedbackStatusFeedbackYnUserIdResponse;
import com.example.demo.src.file.dto.response.FeedbackStatusTeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMembers,Long> {

    @Query("SELECT tm FROM TeamMembers tm WHERE tm.teams.id = :teamId AND tm.users.id = :memberId")
    TeamMembers findByTeamsIdAndUsersId(@Param("teamId") Long teamId, @Param("memberId") Long memberId);

    @Query("SELECT tm.users FROM TeamMembers tm WHERE tm.teams.id = :teamId")
    List<Members> findTeamById(@Param("teamId") Long teamId);

    @Query("SELECT NEW com.example.demo.src.file.dto.response.FeedbackStatusTeamMember(tm.users.id,tm.users.userName,tm.users.pictureUrl,tm.users.studentNumber)  FROM TeamMembers tm WHERE tm.teams.id = :teamId")
    List<FeedbackStatusTeamMember> findUserNameAndUserIdAndPictureUrlByTeamsId(@Param("teamId") Long teamId);
}
