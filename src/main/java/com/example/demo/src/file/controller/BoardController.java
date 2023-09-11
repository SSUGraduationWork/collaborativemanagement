//코드 수정할거 request,response DTO로 변환


package com.example.demo.src.file.controller;

import com.example.demo.src.file.Repository.FileRepository;
import com.example.demo.src.file.Service.BoardService;
import com.example.demo.src.file.common.CommonCode;
import com.example.demo.src.file.common.Response;
import com.example.demo.src.file.domain.Files;
import com.example.demo.src.file.dto.request.BoardWriteRequest;
import com.example.demo.src.file.dto.response.BoardDetailResponse;
import com.example.demo.src.file.dto.response.BoardResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@AllArgsConstructor
//@CrossOrigin(origins="http://localhost:8081")
public class BoardController {

    private final BoardService boardService;
    private FileRepository fileRepository;

    //파일 한번에 여러게 업로드 및 게시글 작성
    @PostMapping("/board/upcount/{boardId}")
    public void upcount( @PathVariable("boardId") Long boardId ) {
        //조회수 증가 로직
        boardService.increaseCount(boardId);
    }

    //파일 한번에 여러게 업로드 및 게시글 작성
    @PostMapping("/board/multiWrite/{memberId}/{teamId}/{workId}")
    public void multipleBoardWriteForm(@Valid BoardWriteRequest request,
                                                                  @PathVariable("memberId") Long memberId,
                                                                  @PathVariable("teamId") Long teamId,
                                                                  @PathVariable("workId") Long workId,
                                                                  @PathVariable(value = "files", required = false) MultipartFile[] files) throws Exception{
         boardService.multiWrite(request,memberId,teamId,workId,files);
    }


    //게시글 리스트+페이징 추후 필요+정렬 필요
    @GetMapping("/board/list/{memberId}/{teamId}")
    public ResponseEntity<Response<List<BoardResponse>>> boardList(
            @PathVariable("memberId") Long memberId,
            @PathVariable("teamId") Long teamId) {

        List<BoardResponse> boardResponses = boardService.boardList(memberId, teamId);
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, boardResponses));
    }


    //특정 게시글 눌렀을 때 상세 페이지 생성
    @GetMapping("/board/view/{boardId}/{memberId}/{teamId}")
    public ResponseEntity<Response<BoardDetailResponse>> boardDetailView(@PathVariable("boardId") Long id,@PathVariable("memberId") Long memberId,
                                                                         @PathVariable("teamId") Long teamId){

        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, boardService.boardView(id)));
    }

    //게시판 삭제
    @DeleteMapping("board/delete/{boardId}")
    public String boardDelete(@PathVariable("boardId") Long id){

       boardService.boardDelete(id);
       return "삭제 성공";
    }

    @DeleteMapping("/files/delete")
    public ResponseEntity<String> deleteFiles(@RequestBody List<Long> fileIdList) {
        try {
            boardService.deleteFileSystem(fileIdList); // 서비스 메서드 호출
            return ResponseEntity.ok("파일 삭제 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 삭제 실패");
        }
    }



    //다중 파일 게시판 수정
    @PostMapping("/multiboard/update/{boardId}/{memberId}/{teamId}/{workId}") //mod_compl, 수정을 완료했는지
    public ResponseEntity<Response<BoardResponse>> multiboardModify(@PathVariable("boardId") Long boardId,
                                                                    @PathVariable("memberId") Long memberId,
                                                                    @PathVariable("teamId") Long teamId,
                                                                    @PathVariable("workId") Long workId,
                                                               @Valid BoardWriteRequest request,
                                                               @RequestParam("files") MultipartFile[] files) throws Exception {
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, boardService.multiReWrite(boardId,workId,request, files )));
    }




    //파일 다운로드
    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Long fileId, HttpServletRequest request) throws IOException {

        Files files = fileRepository.findById(fileId).get();

        Resource resource = new FileUrlResource("src/main/resources/static"+files.getFilepath());
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + files.getFilename() + "\"")
                .body(resource);
    }
}


