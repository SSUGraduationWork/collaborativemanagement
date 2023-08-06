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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    private  AlarmRepository alarmRepository;
    private  FileRepository fileRepository;

    //글 작성
    public BoardResponse write(BoardWriteRequest request,Long memberId,Long teamId, Long workId, MultipartFile file) throws Exception{
        //게시판 등록
        String projectPath=System.getProperty("user.dir")+ "\\src\\main\\resources\\static\\files";
        Members members = memberRepository.findById(memberId).get();
        Teams teams = teamRepository.findById(teamId).get();
        Works works = workRepository.findById(workId).get();
        UUID uuid= UUID.randomUUID();
        String fileName=uuid+"_"+file.getOriginalFilename();
        File saveFile =new File(projectPath, fileName);
        file.transferTo(saveFile);

        Boards boards =toEntity(request);
        boards.confirmMember(members);
        boards.confirmTeam(teams);
        boards.confirmWork(works);

        boardRepository.save(boards);


        // 빌더를 사용하여 파일 객체 생성
        Files file1 = Files.builder()
                .filename(fileName)
                .filepath("/files/" + fileName)
                .build();
        file1.confirmBoard(boards);
        fileRepository.save(file1);
        //멤버수에 맞는 feedbackstatus 테이블 등록 및 글 생성 알람 메시지 저장
        FeedbackStatusAndAlarm(boards, members,works);


        return BoardResponse.from(boards);
    }

    //글 작성
    public BoardResponse multiWrite(BoardWriteRequest request, Long memberId, Long teamId, Long workId, MultipartFile[] files) throws Exception {
        // 게시판 등록
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        Members members = memberRepository.findById(memberId).get();
        Teams teams = teamRepository.findById(teamId).get();
        Works works = workRepository.findById(workId).get();

        Boards boards = toEntity(request);
        boards.confirmMember(members);
        boards.confirmTeam(teams);
        boards.confirmWork(works);

        boardRepository.save(boards);
        // Save all uploaded files
        for (MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);


            // 빌더를 사용하여 파일 객체 생성
            Files file1 = Files.builder()
                    .filename(fileName)
                    .filepath("/files/" + fileName)
                    .build();
            file1.confirmBoard(boards);
            fileRepository.save(file1);

        }
        // 멤버수에 맞는 feedbackstatus 테이블 등록 및 글 생성 알람 메시지 저장
        FeedbackStatusAndAlarm(boards, members, works);
        return BoardResponse.from(boards);
    }


    public void FeedbackStatusAndAlarm(Boards boards, Members writers,Works works){
        // 해당 팀에 속한 모든 멤버 가져와서 FeedbackStatuses에 추가
        List<Members> allMembers = boards.getTeams().getAllMembers();
        for (Members member : allMembers) {
            //만약 글 작성자 본인이라면 피드백 승인,거부를 할 필요가 없으므로 feedbackStatus 등록 필요 없음.
            if(member.equals(writers)){
                continue;
            }
            // FeedbackStatuses 엔티티 생성 및 저장
            FeedbackStatuses feedbackStatuses = new FeedbackStatuses();
            feedbackStatuses.confirmBoard(boards); // 연관관계 설정
            feedbackStatuses.setFeedbackYn(false); // 예시로 피드백 상태를 false로 설정


            feedbackStatuses.confirmMember(member);

            feedbackStatusRepository.save(feedbackStatuses);

            //알림 기능, 글 등록 시 모든 팀의 모든 팀원들에게 알람이 감
            String userName = writers.getUserName(); //글 작성자 이름
            Integer studentNumber = writers.getStudentNumber(); //학번
            String workName=works.getWorkName();
            String title=boards.getTitle();

            //알람 메시지 등록
             String message ="'"+studentNumber+" "+userName+"'님께서 '["+workName+"]" + title + "'에 대해 새로운 글을 등록 하였습니다.";
             String url="/board/view/"+boards.getId();
            Alarms alarms = new Alarms();
            alarms.confirmMember(member); // 연관관계 설정
            alarms.setContent(message);
            alarms.setRedirectUrl(url);

            alarmRepository.save(alarms);
        }
    }



    public static Boards toEntity(BoardWriteRequest boardWriteRequest) {
        return Boards.builder()
                .title(boardWriteRequest.getTitle())
                .content(boardWriteRequest.getContent())
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




    //다중 파일 글 재작성
    public BoardResponse multiReWrite(Long boardId,BoardWriteRequest request, MultipartFile[] files, boolean mod_compl) throws Exception{
        String projectPath=System.getProperty("user.dir")+ "\\src\\main\\resources\\static\\files";

        Boards boards= boardRepository.findById(boardId).get();

        boards.setTitle(request.getTitle());
        boards.setContent(request.getContent());


        //기존에 저장되어 있던 파일들은 지우고 시작
        deletePhotoFromFileSystem(boards.getFileList());

        // 새로 업로드 될 파일
        for (MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);


            // 빌더를 사용하여 파일 객체 생성
            Files file1 = Files.builder()
                    .filename(fileName)
                    .filepath("/files/" + fileName)
                    .build();
            file1.confirmBoard(boards);
            fileRepository.save(file1);

        }


        //조회수 증가 로직
        boards.updateViewCount(boards.getViewCnt());

        boardRepository.save(boards);

        if(mod_compl==true)
        {//글작성자 제외 팀원 모두에게 알람이 가도록
            completionAlarm(boards);
        }
        return BoardResponse.from(boards);
    }



    //단일 파일 글 재작성
    public BoardResponse reWrite(Long boardId,BoardWriteRequest request, MultipartFile file, boolean mod_compl) throws Exception{
        String projectPath=System.getProperty("user.dir")+ "\\src\\main\\resources\\static\\files";

        //Members members = memberRepository.findById(memberId).get();
       // Teams teams = teamRepository.findById(teamId).get();
        //Works works = workRepository.findById(workId).get();
        Boards boards= boardRepository.findById(boardId).get();

        UUID uuid= UUID.randomUUID();
        String fileName=uuid+"_"+file.getOriginalFilename();
        File saveFile =new File(projectPath, fileName);
        file.transferTo(saveFile);
        boards.setTitle(request.getTitle());
        boards.setContent(request.getContent());

        List<Files> fileList = boards.getFileList();

        // 파일 리스트가 비어있지 않은 경우에만 첫 번째 파일을 가져옴
        if (!fileList.isEmpty()) {
            Files firstFile = fileList.get(0);
            // 첫 번째 파일에 대한 로직 처리

            //기존에 저장되어 있던 파일들은 지우고 시작
            OneDeletePhotoFromFileSystem(firstFile);
        }

        // 빌더를 사용하여 파일 객체 생성
        Files files = Files.builder()
                .filename(fileName)
                .filepath("/files/" + fileName)
                .build();
        files.confirmBoard(boards);
        //조회수 증가 로직
        boards.updateViewCount(boards.getViewCnt());

        boardRepository.save(boards);

        if(mod_compl==true)
        {//글작성자 제외 팀원 모두에게 알람이 가도록
            completionAlarm(boards);
        }
        return BoardResponse.from(boards);
    }

    public void completionAlarm(Boards boards){
        // 해당 팀에 속한 모든 멤버 가져와서 FeedbackStatuses에 추가
        List<Members> allMembers = boards.getTeams().getAllMembers();
        List<FeedbackStatuses> feedbackStatuses =feedbackStatusRepository.findByBoards(boards);

        for (FeedbackStatuses feedbackStatuse : feedbackStatuses) {
            //만약 글 작성자 본인이라면 피드백 승인,거부를 할 필요가 없으므로 feedbackStatus 등록 필요 없음.
            //알림 기능, 글 등록 시 모든 팀의 모든 팀원들에게 알람이 감
                String userName = boards.getUsers().getUserName(); //글 작성자 이름
                Integer studentNumber = boards.getUsers().getStudentNumber(); //학번
                String workName=boards.getWorks().getWorkName();
                String title=boards.getTitle();

                //알람 메시지 등록
                String message ="'"+studentNumber+" "+userName+"'님께서 '["+workName+"]" + title + "' 피드백을 반영하여 수정하였습니다.";
                String url="/board/view/"+boards.getId();
                Alarms alarms = new Alarms();
                alarms.confirmMember(feedbackStatuse.getUsers()); // 연관관계 설정
                alarms.setContent(message);
                alarms.setRedirectUrl(url);

                alarmRepository.save(alarms);

        }
    }

    //특정 파일 다운로드 할때 관련 게시판 찾기
    public Boards boardFind(Long id){
        Boards boards= boardRepository.findById(id).get();
        return boards;
    }

    //특정 게시글 삭제
    public void boardDelete(Long id){
        Boards boards= boardRepository.findById(id).get();

        deletePhotoFromFileSystem(boards.getFileList());
        boardRepository.deleteById(id);
    }

    private void deletePhotoFromFileSystem(List<Files> files) {
        try {
            for (Files file : files) {
                String photoPath = file.getFilepath();
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
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("사진 파일 삭제 중 오류 발생: ");
        }
    }



    private void OneDeletePhotoFromFileSystem(Files file) {
        try {

                String photoPath = file.getFilepath();
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
            System.err.println("사진 파일 삭제 중 오류 발생: ");
        }
    }





}
