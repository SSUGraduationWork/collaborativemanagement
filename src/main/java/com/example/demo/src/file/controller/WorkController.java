package com.example.demo.src.file.controller;


import com.example.demo.src.file.Service.WorkService;
import com.example.demo.src.file.common.CommonCode;
import com.example.demo.src.file.common.Response;
import com.example.demo.src.file.dto.response.WorkResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
//@CrossOrigin(origins="http://localhost:8081")
public class WorkController {
    private final WorkService workService;

    //게시글 리스트+페이징 추후 필요+정렬 필요
    @GetMapping("/work/list/{memberId}/{teamId}")
    public ResponseEntity<Response<List<WorkResponse>>> boardList(
            @PathVariable("memberId") Long memberId,
            @PathVariable("teamId") Long teamId){

        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, workService.workList(memberId,teamId)));
    }

}
