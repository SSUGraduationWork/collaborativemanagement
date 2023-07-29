package com.example.upload.Service;

import com.example.upload.Repository.BoardRepository;
import com.example.upload.domain.Board;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class BoardService {


    private BoardRepository boardRepository;
    //글 작성
    public void write(Board board, MultipartFile file) throws Exception{
        String projectPath=System.getProperty("user.dir")+ "\\src\\main\\resources\\static\\files";

        UUID uuid= UUID.randomUUID();
        String fileName=uuid+"_"+file.getOriginalFilename();
        File saveFile =new File(projectPath, fileName);
        file.transferTo(saveFile);

        board.setFilename(fileName);
        board.setFilepath("/files/"+fileName);
        boardRepository.save(board);
    }
   //게시글 리스트 처리리
   public Page<Board> boardList(Pageable pageable){
        return boardRepository.findAll(pageable);
    }
    //특정 게시글 불러오기
    public Board boardView(Long id){
        Board board= boardRepository.findById(id).get();
        board.updateViewCount(board.getViewCount());
        return board;
    }

    //특정 게시글 삭제
    public void boardDelete(Long id){
        boardRepository.deleteById(id);
    }






}
