//코드 수정할거 request,response DTO로 변환


package com.example.upload.controller;

import com.example.upload.Service.BoardService;

import com.example.upload.common.CommonCode;
import com.example.upload.common.Response;
import com.example.upload.domain.Boards;
import com.example.upload.dto.request.BoardWriteRequest;
import com.example.upload.dto.response.BoardDetailResponse;
import com.example.upload.dto.response.BoardResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
@RestController
@AllArgsConstructor
public class BoardController {


    private final BoardService boardService;
    //게시글 작성
   @PostMapping("/board/write/{memberId}/{teamId}/{workId}")
    public ResponseEntity<Response<BoardResponse>> boardWriteForm(@Valid BoardWriteRequest request,
                                                                  @PathVariable("memberId") Long memberId,
                                                                  @PathVariable("teamId") Long teamId,
                                                                  @PathVariable("workId") Long workId,
                                                                  @RequestParam("file") MultipartFile file) throws Exception{
       return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, boardService.write(request,memberId,teamId,workId,file)));
    }

    //게시글 리스트+페이징 추후 필요+정렬 필요
    @GetMapping("/board/list")
    public ResponseEntity<Response<Page<BoardResponse>>> boardList(@PageableDefault(page=0,size=10,sort="id",direction= Sort.Direction.DESC) Pageable pageable){

        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, boardService.boardList(pageable)));
    }

    //특정 게시글 눌렀을 때 상세 페이지 생성
    @GetMapping("/board/view/{id}")
    public ResponseEntity<Response<BoardDetailResponse>> boardDetailView(@PathVariable Long id){

        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, boardService.boardView(id)));
    }

    //게시판 삭제
    @DeleteMapping("board/delete/{id}")
    public String boardDelete(@PathVariable Long id){

       boardService.boardDelete(id);
       return "삭제 성공";
    }


    //게시판 수정
    @PostMapping("/board/update/{boardId}/{memberId}/{teamId}/{workId}")
    public ResponseEntity<Response<BoardResponse>> boardModify(@PathVariable("boardId") Long boardId,
                                                               @Valid BoardWriteRequest request,
                                                               @PathVariable("memberId") Long memberId,
                                                               @PathVariable("teamId") Long teamId,
                                                               @PathVariable("workId") Long workId,
                                                               @RequestParam("file") MultipartFile file) throws Exception {
        Boards boardTemp= boardService.boardUpdate(boardId);
        boardTemp.setTitle(request.getTitle());
        boardTemp.setContent(request.getContent());
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, boardService.reWrite(boardTemp,memberId,teamId,workId,file)));
    }




 //파일 다운로드
    @GetMapping("/downloadFile/{boardId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("boardId") Long boardId, HttpServletRequest request) throws IOException {
        Boards board = boardService.boardFind(boardId);
        if (board == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileUrlResource("src/main/resources/static"+board.getFilepath());
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + board.getFilename() + "\"")
                .body(resource);
    }
}


