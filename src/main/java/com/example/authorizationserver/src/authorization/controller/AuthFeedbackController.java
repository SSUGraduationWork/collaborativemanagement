package com.example.authorizationserver.src.authorization.controller;

import com.example.authorizationserver.config.BaseException;
import com.example.authorizationserver.src.authorization.service.AuthService;
import com.example.authorizationserver.src.authorization.service.RequestService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthFeedbackController {
    private final AuthService authService;
    private final RequestService requestService;
    @Value("${api-server.spring.domain}")
    private String domain;

    // 피드백 조회 : [GET] /comment/{boardId}/{memberId}/{teamId}
    @GetMapping("/comment/{boardId}/{teamId}")
    public JsonNode authGetFeedbacks(HttpServletRequest request,
                                     @PathVariable("boardId") Long boardId,
                                     @PathVariable("teamId") Long teamId) throws BaseException{

        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        if (userId != null && role != null) {
            Boolean permission = authService.checkViewPermission(teamId, userId, role);
            if (permission) {
                String url = domain + "comment/" + boardId + "/" + userId + "/" +teamId;
                JsonNode result =  requestService.requestTo(url, "GET", null);
                ((ObjectNode) result).put("userId", userId);

                return result;
            }
        }
        return null;
    }

    // 피드백 글쓰기 : [POST] /comment/{boardId}/{writerId}/{isApproved}
    @PostMapping("/comment/{teamId}/{boardId}/{isApproved}")
    public JsonNode authPostFeedback(HttpServletRequest request,
                                      @PathVariable(value = "teamId") Long teamId,
                                      @PathVariable(value = "boardId") Long boardId,
                                      @PathVariable(value = "isApproved") Integer isApproved,
                                      @RequestBody JsonNode jsonData) throws BaseException {

        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        System.out.println(jsonData);
        if (userId != null && role != null) {
            Boolean permission = authService.checkPostPermission(teamId, userId, role);
            if (permission) {
                String url = domain + "comment/" + boardId + "/" + userId + "/" +isApproved;
                JsonNode result =  requestService.requestTo(url, "POST", jsonData);
                System.out.println(result);
                return result;
            }
        }
        return null;
    }

    //피드백 반영하여 수정한 게시글에 대한 수락, 거절 여부 : [POST] /recomment/{boardId}/{writerId}/{isApproved}
    //이때는 writerId와 userId가 같은지 확인.
    @PostMapping("/recomment/{boardId}/{writerId}/{isApproved}")
    public void authRepostFeedback(HttpServletRequest request,
                                             @PathVariable("boardId") Long boardId,
                                             @PathVariable("writerId") Long writerId,
                                             @PathVariable("isApproved") Integer isApproved) throws BaseException{

        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        if (userId != null && role.equals("student")) {
            if (userId.equals(writerId)) {
                String url = domain + request.getRequestURI();
                requestService.requestTo(url, "POSTReturnVoid", null);
            }
        }
    }
}
