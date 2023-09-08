package com.example.demo.src.file.Service;

import com.example.demo.src.file.Repository.BoardRepository;
import com.example.demo.src.file.Repository.TeamRepository;
import com.example.demo.src.file.Repository.WorkRepository;
import com.example.demo.src.file.Repository.WorkerRepository;
import com.example.demo.src.file.domain.Boards;
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
private BoardRepository boardRepository;
private WorkerRepository workerRepository;
    public List<WorkResponse> workList(Long userId,Long teamId){
        List<Works> workList=workerRepository.findAllByUserIdAndTeamId(userId,teamId);


        return workList.stream()
                .map(work-> WorkResponse
                        .builder()
                        .workId(work.getId())
                        .workName(work.getWorkName())
                        .build())
                .collect(Collectors.toList());
    }

}
