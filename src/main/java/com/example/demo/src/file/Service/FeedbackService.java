package com.example.demo.src.file.Service;



import com.example.demo.src.file.Repository.*;
import com.example.demo.src.file.domain.*;
import com.example.demo.src.file.dto.request.FeedbackRequest;
import com.example.demo.src.file.dto.response.BoardFeedbackResponse;
import com.example.demo.src.file.dto.response.FeedbackResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@AllArgsConstructor
public class FeedbackService {


    private FeedbackRepository feedbackRepository;
    private BoardRepository boardRepository;

    private MembersRepository membersRepository;
    private FeedbackStatusRepository feedbackStatusRepository;

    private final AlarmRepository alarmRepository;
    public FeedbackResponse save(Long boardId, Long memberId, FeedbackRequest requset, boolean isApproved) {
        //피드백 등록
        Boards boards =boardRepository.findById(boardId).get();
        Members writers = membersRepository.findById(memberId).get();
        Feedbacks feedbacks = toEntity(requset);
        feedbacks.confirmBoard(boards);
        feedbacks.confirmMember(writers);
        feedbackRepository.save(feedbacks);

        //피드백 작성자가 동의 하면 feedbackStauses의 feedback_yn=true로 변경
        OneTeaamMemberAgreeCheck(boards, writers,isApproved);

        // 팀원 모두 동의하면 boards의 feedback_yn=true로 변경
        AllTeamMemberAgreeCheck(boards);

        return FeedbackResponse.from(feedbacks, boards);
    }

    public  void OneTeaamMemberAgreeCheck(Boards boards, Members writers, boolean isApproved){
        //boards, writers로 feedbackStatus찾기
        FeedbackStatuses feedbackStatus =feedbackStatusRepository.findByBoardsAndUsers(boards, writers);
        if (isApproved) {
            // 승인한 경우 feedbackYn=true로 바꾸기
            feedbackStatus.feedbackAgree();
        } else {
            // 거부한 경우 feedbackYn=false로 바꾸기
            feedbackStatus.feedbackDeny();

            //게시판 작성자에게 알람이 가도록 함
            FeedbackStatusAndAlarm(boards, writers);

        }
    }

    //멤버수에 맞는 feedbackstatus 테이블 등록 및 글 생성 알람 메시지 저장
    public void FeedbackStatusAndAlarm(Boards boards, Members writers ){ //wrtier은 feedback작성자

            //알림 기능, 글 등록 시 모든 팀의 모든 팀원들에게 알람이 감
            String userName = writers.getUserName(); //피드백 작성자 이름
            Integer studentNumber = writers.getStudentNumber(); // 피드백 작성자 학번
            String workName= boards.getWorks().getWorkName();//작업이름
            String title= boards.getTitle(); //게시판 제목



            String message ="'"+studentNumber+" "+userName+"'님께서 '["+workName+"]" + title + "'에 대해 수정 요청을 하였습니다.";
            String url="/boards/view/"+ boards.getId();

        List<Members> allMembers = boards.getTeams().getAllMembers();
        for (Members member : allMembers) {

            if (member.equals(writers)) {continue;}
            Alarms alarms = new Alarms();
            alarms.confirmMember(member); // 연관관계 설정, board의 작성자로 저장
            alarms.setContent(message);
            alarms.setRedirectUrl(url);
            alarms.setWriterPictureUrl(writers.getPictureUrl());
            alarms.setAlarmKind("requestFeedback");
            alarms.confirmBoard(boards);
            alarms.setWriterId(writers.getId());
            alarmRepository.save(alarms);
        }
    }







    //???? 완료 되었을 때도 알람이 가게 할지??
    public  void AllTeamMemberAgreeCheck(Boards boards){

        // Board에 해당하는 모든 FeedbackStatuses 조회
        List<FeedbackStatuses> feedbackStatusesList = feedbackStatusRepository.findByBoards(boards);

        // FeedbackStatuses가 존재하는 경우에만 처리
        if (!feedbackStatusesList.isEmpty()) {
            boolean hasFeedbackYnTrue = true;

            // 모든 FeedbackStatuses의 feedback_yn이 true인지 확인
            for (FeedbackStatuses feedbackStatuses : feedbackStatusesList) {
                if (!feedbackStatuses.isFeedbackYn()) { //한명이라도 feedback을 안했으면
                    hasFeedbackYnTrue = false;
                    break;
                }
            }

            // 모든 FeedbackStatuses의 feedback_yn이 true라면 board의 feedback_yn도 true로 변경
            if (hasFeedbackYnTrue) {
                boards.setFeedbackYn(true);
                boardRepository.save(boards);
                //팀원 모두에게 피드백 완료 알람 전송
                allFeedbackCompleteAlarm(boards);
            }
        }
    }

    public void allFeedbackCompleteAlarm(Boards boards){ //wrtier은 feedback작성자

        //알림 기능, 글 등록 시 모든 팀의 모든 팀원들에게 알람이 감

        String workName= boards.getWorks().getWorkName();//작업이름
        String title= boards.getTitle(); //게시판 제목

        String message ="'["+workName+"]" + title + "'에 대한 모든 피드백이 완료되어 '완료' 상태가 되었습니다.";
        String url="/boards/view/"+ boards.getId();

        // 해당 팀에 속한 모든 멤버 가져와서 FeedbackStatuses에 추가
        List<Members> allMembers = boards.getTeams().getAllMembers();
        for (Members member : allMembers) {
            Alarms alarms = new Alarms();
            alarms.confirmMember(member); // 연관관계 설정, board의 작성자로 저장
            alarms.setContent(message);
            alarms.setRedirectUrl(url);
            alarms.setAlarmKind("complFeedback");
            alarms.confirmBoard(boards);
            alarmRepository.save(alarms);
        }

    }


    public static Feedbacks toEntity(FeedbackRequest feedbackRequest) {
        return Feedbacks.builder()
                .comment(feedbackRequest.getComment())
                .build();
    }


    public void reFeedback(Long boardId, Long memberId, boolean isApproved) { //재수락할지, 거절할지
        Boards boards =boardRepository.findById(boardId).get();
        Members writers = membersRepository.findById(memberId).get();

        //boards, writers로 feedbackStatus찾기
        FeedbackStatuses feedbackStatus =feedbackStatusRepository.findByBoardsAndUsers(boards, writers);
        if (isApproved) {
            // 승인한 경우 feedbackYn=true로 바꾸기
            feedbackStatus.feedbackAgree();

            //게시판 작성자에게 수락 알람이 가도록 함
            reFeedbackAlarmAgree(boards, writers);
        } else {
            // 거부한 경우 feedbackYn=false로 바꾸기
            feedbackStatus.feedbackDeny();

            //게시판 작성자에게 거절 알람이 가도록 함
            reFeedbackAlarmDeny(boards, writers);

        }
        // 팀원 모두 동의하면 boards의 feedback_yn=true로 변경
        AllTeamMemberAgreeCheck(boards);
    }


    public void reFeedbackAlarmDeny(Boards boards, Members writers){ //wrtier은 feedback작성자

        //알림 기능, 글 등록 시 모든 팀의 모든 팀원들에게 알람이 감
        String userName = writers.getUserName(); //피드백 작성자 이름
        Integer studentNumber = writers.getStudentNumber(); // 피드백 작성자 학번
        String workName= boards.getWorks().getWorkName();//작업이름
        String title= boards.getTitle(); //게시판 제목

        List<Members> allMembers = boards.getTeams().getAllMembers();
        String message = "'" + studentNumber + " " + userName + "'님께서 '[" + workName + "]" + title + "'작성자님의 수정에 대해 거절을 하였습니다.";
        String url = "/boards/view/" + boards.getId();
        for (Members member : allMembers) {

            Alarms alarms = new Alarms();
            alarms.confirmMember(member); // 연관관계 설정, board의 작성자로 저장->모든 멤버들한테 가도록
            alarms.setContent(message);
            alarms.setRedirectUrl(url);
            alarms.setAlarmKind("denyFeedback");
            alarms.confirmBoard(boards);
            alarms.setWriterPictureUrl(writers.getPictureUrl());
            alarms.setWriterId(writers.getId());
            alarmRepository.save(alarms);}

    }

    public void reFeedbackAlarmAgree(Boards boards, Members writers ){ //wrtier은 feedback작성자

        //알림 기능, 글 등록 시 모든 팀의 모든 팀원들에게 알람이 감
        String userName = writers.getUserName(); //피드백 작성자 이름
        Integer studentNumber = writers.getStudentNumber(); // 피드백 작성자 학번
        String workName= boards.getWorks().getWorkName();//작업이름
        String title= boards.getTitle(); //게시판 제목

        List<Members> allMembers = boards.getTeams().getAllMembers();

        String message ="'"+studentNumber+" "+userName+"'님께서 '["+workName+"]" + title + "'작성자님의 수정에 대해 수락하였습니다.";
        String url="/boards/view/"+ boards.getId();
        for (Members member : allMembers) {

                Alarms alarms = new Alarms();
                alarms.confirmMember(member); // 연관관계 설정, board의 작성자로 저장
                alarms.setContent(message);
                alarms.setRedirectUrl(url);
                alarms.setAlarmKind("agreeFeedback");
                alarms.confirmBoard(boards);
                alarms.setWriterPictureUrl(writers.getPictureUrl());
                alarms.setWriterId(writers.getId());
                alarmRepository.save(alarms);

        }
    }

    //피드백 불러오기
    //이중 반복문보다 좋은 방법이 있을지 생각해보기
    public List<BoardFeedbackResponse> feedbackView(Long boardId) {
        List<Feedbacks> feedbacksList = feedbackRepository.findAllByBoardsId(boardId);
        List<FeedbackStatuses> feedbackStatusesList=feedbackStatusRepository.findAllByBoardsId(boardId);

        List<BoardFeedbackResponse> boardFeedbackResponses = new ArrayList<>();

        for(Feedbacks feedbacks : feedbacksList){
            for(FeedbackStatuses feedbackStatus: feedbackStatusesList ){
                if(feedbackStatus.getUsers()== feedbacks.getWriters()){
                    BoardFeedbackResponse boardFeedbackResponse= BoardFeedbackResponse
                            .builder()
                            .comment(feedbacks.getComment())
                            .feedbackId(feedbacks.getId())
                            .studentNumber(feedbacks.getWriters().getStudentNumber())
                            .userName(feedbacks.getWriters().getUserName())
                            .pictureUrl(feedbacks.getWriters().getPictureUrl())
                            .createdAt(feedbacks.getCreatedAt())
                            .modReq(feedbackStatus.isFeedbackYn())
                            .build();
                    boardFeedbackResponses.add(boardFeedbackResponse);
                }
            }
        }

        return boardFeedbackResponses;
    }
}
