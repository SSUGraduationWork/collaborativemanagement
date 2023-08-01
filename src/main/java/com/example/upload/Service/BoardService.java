package com.example.upload.Service;

import com.example.upload.Repository.*;

import com.example.upload.domain.*;

import com.example.upload.dto.request.BoardWriteRequest;
import com.example.upload.dto.response.BoardDetailResponse;
import com.example.upload.dto.response.BoardResponse;
import com.example.upload.dto.response.FeedbackResponse;
import lombok.AllArgsConstructor;

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
    private MemberRepository memberRepository;
    private TeamRepository teamRepository;
    private WorkRepository workRepository;
    private FeedbackStatusRepository feedbackStatusRepository;
    //글 작성
    public BoardResponse write(BoardWriteRequest request,Long memberId,Long teamId, Long workId, MultipartFile file) throws Exception{
        String projectPath=System.getProperty("user.dir")+ "\\src\\main\\resources\\static\\files";
        Members members = memberRepository.findById(memberId).get();
        Teams teams = teamRepository.findById(teamId).get();
        Works works = workRepository.findById(workId).get();
      //  List<Long> MemberIdList= feedbackStatusRepository.findMemberIdsByTeamId(teamId);
        UUID uuid= UUID.randomUUID();
        String fileName=uuid+"_"+file.getOriginalFilename();
        File saveFile =new File(projectPath, fileName);
        file.transferTo(saveFile);
        Boards boards =toEntity(request,fileName);
        boards.confirmMember(members);
        boards.confirmTeam(teams);
        boards.confirmWork(works);

        boardRepository.save(boards);





        // 해당 팀에 속한 모든 멤버 가져와서 FeedbackStatuses에 추가
        List<Members> allMembers = boards.getTeams().getAllMembers();

        for (Members member : allMembers) {

            // FeedbackStatuses 엔티티 생성 및 저장
            FeedbackStatuses feedbackStatuses = new FeedbackStatuses();
            feedbackStatuses.confirmBoard(boards); // 연관관계 설정
            feedbackStatuses.setFeedbackYn(false); // 예시로 피드백 상태를 false로 설정


            feedbackStatuses.confirmMember(member);

            feedbackStatusRepository.save(feedbackStatuses);
        }







        return BoardResponse.from(boards);
    }

    //글 재작성
    public BoardResponse reWrite(Boards boards,Long memberId,Long teamId, Long workId, MultipartFile file) throws Exception{
        String projectPath=System.getProperty("user.dir")+ "\\src\\main\\resources\\static\\files";

        Members members = memberRepository.findById(memberId).get();
        Teams teams = teamRepository.findById(teamId).get();
        Works works = workRepository.findById(workId).get();

        UUID uuid= UUID.randomUUID();
        String fileName=uuid+"_"+file.getOriginalFilename();
        File saveFile =new File(projectPath, fileName);
        file.transferTo(saveFile);

        boards.setFilename(fileName);
        boards.setFilepath("/files/"+fileName);

        boards.confirmMember(members);
        boards.confirmTeam(teams);
        boards.confirmWork(works);
        boardRepository.save(boards);
        return BoardResponse.from(boards);
    }

    public static Boards toEntity(BoardWriteRequest boardWriteRequest, String fileName) {
        return Boards.builder()
                .title(boardWriteRequest.getTitle())
                .content(boardWriteRequest.getContent())
                .filename(fileName)
                .filepath("/files/" + fileName)
                .build();
    }

    //게시글 리스트 처리리
    public Page<BoardResponse> boardList(Pageable pageable) {
        Page<Boards> boardsPage = boardRepository.findAll(pageable);
        return boardsPage.map(BoardResponse::from);
    }



    //특정 게시글 불러오기
    public BoardDetailResponse boardView(Long id){
        Boards boards= boardRepository.findById(id).get();
        boards.updateViewCount(boards.getViewCnt());
        return BoardDetailResponse.from(boards);
    }



    //특정 게시글 불러오기 -> 업데이트 board controller에서 필요
    public Boards boardUpdate(Long id){
        Boards boards= boardRepository.findById(id).get();
        boards.updateViewCount(boards.getViewCnt());
        return boards;
    }

    //특정 파일 다운로드 할때 관련 게시판 찾기
    public Boards boardFind(Long id){
        Boards boards= boardRepository.findById(id).get();
        return boards;
    }

    //특정 게시글 삭제
    public void boardDelete(Long id){
        Boards boards= boardRepository.findById(id).get();
        deletePhotoFromFileSystem(boards.getFilepath());
        boardRepository.deleteById(id);
    }

    private void deletePhotoFromFileSystem(String photoPath) {
        try {

            String projectPath = System.getProperty("user.dir");
            File photoFile = new File(projectPath + "/src/main/resources/static/" + photoPath);



            // 파일이 존재하는지 확인하고 삭제
            if (photoFile.exists()) {
                if (photoFile.delete()) {
                    System.out.println("사진 파일 삭제 성공: " + photoPath);
                } else {
                    System.err.println("사진 파일 삭제 실패: " + photoPath);
                }
            } else {
                System.err.println("해당 경로에 사진 파일이 존재하지 않습니다: " + photoPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("사진 파일 삭제 중 오류 발생: " + photoPath);
        }
    }







}
