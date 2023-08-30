package com.example.demo.src.file.Service;

import com.example.demo.src.file.Repository.TeamRepository;
import com.example.demo.src.file.Repository.WorkRepository;
import com.example.demo.src.file.domain.Teams;
import com.example.demo.src.file.domain.Works;
import com.example.demo.src.file.dto.response.WorkResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WorkService {
    private WorkRepository workRepository;
    private TeamRepository teamRepository;

    public List<WorkResponse> workList(Long teamId){
        Teams teams = teamRepository.findById(teamId).get();
        List<Works> workList = workRepository.findByTeams(teams);
        return workList.stream()
                .map(work-> WorkResponse
                        .builder()
                        .workId(work.getId())
                        .workName(work.getWorkName())
                        .build())
                .collect(Collectors.toList());
    }

}
