package com.example.demo.src.file.Service;


import com.example.demo.src.file.Repository.*;
import com.example.demo.src.file.domain.*;
import com.example.demo.src.file.dto.request.BoardWriteRequest;
import com.example.demo.src.file.dto.response.BoardDetailResponse;
import com.example.demo.src.file.dto.response.BoardResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class BoardService {


    private BoardRepository boardRepository;
    private MembersRepository membersRepository;
    private TeamRepository teamRepository;
    private WorkRepository workRepository;
    private FeedbackStatusRepository feedbackStatusRepository;
    private AlarmRepository alarmRepository;
    private FileRepository fileRepository;

    public void  increaseCount(Long boardId){
        Boards boards = boardRepository.findById(boardId).get();
        boards.updateViewCount(boards.getViewCnt());
        boardRepository.save(boards);
    }

    //글 작성
    public BoardResponse multiWrite(BoardWriteRequest request, Long memberId, Long teamId, Long workId, MultipartFile[] files) throws Exception {
        // 게시판 등록
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        Members members = membersRepository.findById(memberId).get();
        Teams teams = teamRepository.findById(teamId).get();
        Works works = workRepository.findById(workId).get();

        Boards boards = toEntity(request);
        boards.confirmMember(members);
        boards.confirmTeam(teams);
        boards.confirmWork(works);

        boardRepository.save(boards);
        // Save all uploaded files
        if (files != null) {
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
        }
        // 멤버수에 맞는 feedbackstatus 테이블 등록 및 글 생성 알람 메시지 저장
        FeedbackStatusAndAlarm(boards, members, works,teams);
        return BoardResponse.from(boards);
    }


    public void FeedbackStatusAndAlarm(Boards boards, Members writers, Works works, Teams teams){
        // 해당 팀에 속한 모든 멤버 가져와서 FeedbackStatuses에 추가
        List<Members> allMembers = boards.getTeams().getAllMembers();
        for (Members member : allMembers) {

            //수정-----------------------------
            //만약 글 작성자 본인이라면 피드백 승인,거부를 할 필요가 없으므로 feedbackStatus 등록 필요 없음.
            //-> 글 작성자 본인도 피드백 창에 글을 남길 수 있도록 변경
            //글 작성자는 기본적으로 동의한다고 설정
            if(member.equals(writers)){
                FeedbackStatuses feedbackStatuses = new FeedbackStatuses();
                feedbackStatuses.confirmBoard(boards); // 연관관계 설정
                feedbackStatuses.setFeedbackYn(true); // 예시로 피드백 상태를 false로 설정;
                feedbackStatuses.confirmMember(member);
                feedbackStatuses.confirmTeam(teams);
                feedbackStatusRepository.save(feedbackStatuses);
                continue;
            }


            // FeedbackStatuses 엔티티 생성 및 저장
            FeedbackStatuses feedbackStatuses = new FeedbackStatuses();
            feedbackStatuses.confirmBoard(boards); // 연관관계 설정
            feedbackStatuses.setFeedbackYn(false); // 예시로 피드백 상태를 false로 설정


            feedbackStatuses.confirmMember(member);
            feedbackStatuses.confirmTeam(teams);
            feedbackStatusRepository.save(feedbackStatuses);

            //알림 기능, 글 등록 시 모든 팀의 모든 팀원들에게 알람이 감
            String userName = writers.getUserName(); //글 작성자 이름
            Integer studentNumber = writers.getStudentNumber(); //학번
            String workName=works.getWorkName();
            String title= boards.getTitle();

            //알람 메시지 등록
             String message ="'"+studentNumber+" "+userName+"'님께서 '["+workName+"]" + title + "'에 대해 새로운 글을 등록 하였습니다.";
             String url="/boards/view/"+ boards.getId();
            Alarms alarms = new Alarms();
            alarms.setWriterPictureUrl(writers.getPictureUrl());
            alarms.confirmMember(member); // 연관관계 설정
            alarms.setContent(message);
            alarms.setRedirectUrl(url);
            alarms.setAlarmKind("newWrite");
            alarms.confirmBoard(boards);
            alarms.setWriterId(writers.getId());
            alarmRepository.save(alarms);
        }
    }



    public static Boards toEntity(BoardWriteRequest boardWriteRequest) {
        return Boards.builder()
                .title(boardWriteRequest.getTitle())
                .content(boardWriteRequest.getContent())
                .build();
    }




    // 게시글 리스트 처리
    public List<BoardResponse> boardList(Long memberId, Long teamId) {
        // memberId를 사용하여 해당 멤버의 알람 리스트를 가져옴


        // memberId와 teamId에 해당하는 게시글 리스트 조회
        // teamId로 해당 팀의 게시글 리스트 조회
        List<Boards> boardsList = boardRepository.findAllByTeamsId(teamId);
        List<FeedbackStatuses> feedbackStatusesList=feedbackStatusRepository.findByUsersIdAndTeamsId(memberId,teamId);
        int length = boardsList.size();
        int length1=feedbackStatusesList.size();
        System.out.println("리스트의 길이는 " + length + "입니다.");
        System.out.println("리스트의 길이는 " + length1 + "입니다.");
        List<BoardResponse> boardResponses = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            Boards board = boardsList.get(i);
            FeedbackStatuses feedbackStatus=feedbackStatusesList.get(i);
            BoardResponse boardResponse = BoardResponse.from(board);
            boardResponse.setFeedbackYn(feedbackStatus.isFeedbackYn());
            boardResponses.add(boardResponse);
        }
        return boardResponses;
    }


    //특정 게시글 불러오기
    public BoardDetailResponse boardView(Long id){
        Boards boards = boardRepository.findById(id).get();
        return BoardDetailResponse.from(boards);
    }



    //___________________________________________________________
    //일단 수정 요청을 하지 않아도, 팀원 모두에게 수정했다고 알람이 가도록 구현
    //추후에 수정요청을 한 사람에게만 알람이 가도록, 그리고 수정요청을 받지 않더라도 글 작성자가
    //게시글을 수정하고 싶을 경우에도 생각해야함.
    //다중 파일 글 재작성
    public BoardResponse multiReWrite(Long boardId,Long workId,BoardWriteRequest request, MultipartFile[] files) throws Exception{
        String projectPath=System.getProperty("user.dir")+ "\\src\\main\\resources\\static\\files";

        Boards boards = boardRepository.findById(boardId).get();
        Works works = workRepository.findById(workId).get();
        boards.setWorks(works);

        boards.setTitle(request.getTitle());
        boards.setContent(request.getContent());


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


        boardRepository.save(boards);

        //if(mod_compl==true)
        //글작성자 제외 팀원 모두에게 알람이 가도록
        completionAlarm(boards);
        return BoardResponse.from(boards);
    }




    public void completionAlarm(Boards boards){
        // 해당 팀에 속한 모든 멤버 가져와서 FeedbackStatuses에 추가
        List<Members> allMembers = boards.getTeams().getAllMembers();
       // List<FeedbackStatuses> feedbackStatuses =feedbackStatusRepository.findByBoards(boards);

        for (Members member : allMembers) {
            if (member != null&&!member.equals(boards.getUsers())) {
                //만약 글 작성자 본인이라면 피드백 승인,거부를 할 필요가 없으므로 feedbackStatus 등록 필요 없음.
                //알림 기능, 글 등록 시 모든 팀의 모든 팀원들에게 알람이 감

                String userName = boards.getUsers().getUserName(); //글 작성자 이름
                Integer studentNumber = boards.getUsers().getStudentNumber(); //학번
                String workName = boards.getWorks().getWorkName();
                String title = boards.getTitle();

                //알람 메시지 등록
                String message = "'" + studentNumber + " " + userName + "'님께서 '[" + workName + "]" + title + "' 피드백을 반영하여 수정하였습니다.";
                String url = "/boards/view/" + boards.getId();
                Alarms alarms = new Alarms();
                alarms.confirmMember(member); // 연관관계 설정
                alarms.setContent(message);
                alarms.setRedirectUrl(url);
                alarms.setWriterPictureUrl(boards.getUsers().getPictureUrl());
                alarms.setAlarmKind("complUpdate");
                alarms.confirmBoard(boards);
                alarms.setWriterId(boards.getUsers().getId());
                alarmRepository.save(alarms);
            }
        }


    }


    //특정 게시글 삭제
    public void boardDelete(Long id){
        Boards boards = boardRepository.findById(id).get();

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


//fileId로 파일 삭제
    public void deleteFileSystem(List<Long> fileIdList) {
        try {
            for(Long fileId: fileIdList){
                Files file=fileRepository.findById(fileId).get();
                String photoPath = file.getFilepath();
                String projectPath = System.getProperty("user.dir");
                File photoFile = new File(projectPath + "/src/main/resources/static/" + photoPath);

                fileRepository.deleteById(fileId);

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



}
