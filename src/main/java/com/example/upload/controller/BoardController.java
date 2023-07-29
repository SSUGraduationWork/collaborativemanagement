//코드 수정할거 request,response DTO로 변환


package com.example.upload.controller;

import com.example.upload.Service.BoardService;
import com.example.upload.domain.Board;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
   @PostMapping("/board/write")
    public void boardWriteForm(@Valid Board board, @RequestParam("file") MultipartFile file) throws Exception{
       boardService.write(board,file);
    }
    //게시글 리스트+페이징 추후 필요+정렬 필요
    @GetMapping("/board/list")
    public Page<Board> boardList(@PageableDefault(page=0,size=10,sort="id",direction= Sort.Direction.DESC) Pageable pageable){

        return boardService.boardList(pageable);
    }

    //특정 게시글 눌렀을 때 상세 페이지 생성
    @GetMapping("/board/view/{id}")
    public Board boardView(@PathVariable Long id){

       return boardService.boardView(id);
    }

    //게시판 삭제

    @GetMapping("board/delete/{id}")
    public void boardDelete(@PathVariable Long id){
       boardService.boardDelete(id);
    }

    //게시판 수정

    @PostMapping("/board/update/{id}")
    public void boardModify(@PathVariable("id") Long id,@Valid Board board,@RequestParam("file") MultipartFile file) throws Exception {
        Board boardTemp= boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp,file);
    }



 //파일 다운로드
    @GetMapping("/downloadFile/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id, HttpServletRequest request) throws IOException {
        Board board = boardService.boardView(id);
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


