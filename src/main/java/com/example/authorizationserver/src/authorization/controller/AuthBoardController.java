package com.example.authorizationserver.src.authorization.controller;

import com.example.authorizationserver.config.BaseException;
import com.example.authorizationserver.src.authorization.service.AuthService;
import com.example.authorizationserver.src.authorization.service.RequestService;
import com.example.authorizationserver.utils.JwtService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.authorizationserver.config.BaseResponseStatus.SERVER_ERROR;

@RestController
@RequiredArgsConstructor
public class AuthBoardController {
    private final AuthService authService;
    private final RequestService requestService;

    @Value("${api-server.spring.domain}")
    private String domain;

    //전체 게시글 조회 : [GET] /board/list/:memberId/:teamId
    //게시글 상세 조회 : [GET] /board/view/:boardId/:memberId/:teamId
    @GetMapping(value="/board/**")
    public JsonNode authGetBoard(HttpServletRequest request)throws Exception{
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        String requestURI = request.getRequestURI();
        String[] urlArr = requestURI.split("/");
        Long teamId = Long.parseLong(urlArr[urlArr.length-1]);
        String getType = urlArr[2];

        if (userId != null && role != null) {
            Boolean permission = authService.checkViewPermission(teamId, userId, role);
            String url = "";
            if (permission) {
                if (getType.equals("list")){
                    url = domain + "board/list/" + userId + "/" + teamId;
                } else if (getType.equals("view")) {
                    url = domain + "board/view/" + urlArr[3] + "/" + userId + "/" + teamId;
                }

                JsonNode result =  requestService.requestTo(url, "GET", null);
                ((ObjectNode) result).put("userId", userId);
                ((ObjectNode) result).put("role", role);

                return result;
            }
        }
        return null;
    }

    //게시글 등록 : [POST] /board/multiWrite/:memberId/:teamId}/:workId
    @PostMapping(value="/board/multiWrite/{teamId}/{workId}")
    public ResponseEntity<Void> authPostBoard(HttpServletRequest request,
                                              @PathVariable("teamId") Long teamId,
                                              @PathVariable("workId") Long workId,
                                              @RequestParam("title") String title,
                                              @RequestParam("content") String content,
                                              @RequestParam(value="files", required = false) MultipartFile[] files) throws Exception {

        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        System.out.println(title);
        System.out.println(content);
        if (userId != null && role != null) {
            Boolean permission = authService.checkPostPermission(teamId, userId, role);
            if (permission) {
                String url = domain + "/board/multiWrite/" + userId + "/" + teamId + "/" + workId;
                return requestService.requestFormData(url, "POSTForm", title, content, files);
            }
        }
        return null;
    }

    //게시글 수정 : [POST] /multiboard/update/:boardId/:memberId/:teamId/:workId
    @PostMapping(value="/multiboard/update/{boardId}/{teamId}/{workId}")
    public JsonNode authModPost(HttpServletRequest request,
                                @PathVariable("boardId") Long boardId,
                                @PathVariable("teamId") Long teamId,
                                @PathVariable("workId") Long workId,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam(value="files", required = false) MultipartFile[] files) throws BaseException{
        try{
            Long userId = (Long) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");

            if (userId != null && role != null) {
                Boolean permission = authService.checkModPermission(userId, boardId, role);
                if (permission) {
                    String url = domain + "/multiboard/update/" + boardId + "/" + userId + "/" + teamId + "/" + workId;
                    System.out.println(url);
                    JsonNode result = requestService.requestModFormData(url, "POSTForm", title, content, files);
                    System.out.println(result);
                    return result;
                }
            }
            return null;

        } catch(Exception exception){
            System.out.println(exception.getMessage());
            throw new BaseException(SERVER_ERROR);
        }
    }

    //파일 삭제 : [Delete] /files/delete
    @DeleteMapping("/files/delete/{boardId}")
    public JsonNode authDeleteFiles(HttpServletRequest request,
                                    @PathVariable("boardId") Long boardId,
                                    @RequestBody JsonNode fileIdList) throws BaseException{
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        if (userId != null && role != null) {
            Boolean permission = authService.checkModPermission(userId, boardId, role);
            if (permission) {
                String url = domain + "/files/delete";
                JsonNode result = requestService.requestTo(url, "DELETE", fileIdList);
                System.out.println("파일 삭제 : " + result);
                return result;
            }
        }
        return null;
    }

    //파일 다운로드 : [GET] /downloadFile/{fileId}
    @GetMapping("/downloadFile/{teamId}/{fileId}")
    public ResponseEntity<Resource> authDownloadFile(HttpServletRequest request,
                                                     @PathVariable("teamId") Long teamId,
                                                     @PathVariable("fileId") Long fileId) throws BaseException{
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        if (userId != null && role != null){
            Boolean permission = authService.checkViewPermission(teamId, userId, role);
            if(permission){
                String url = domain + "/downloadFile/" + fileId;
                ResponseEntity<Resource> result =  requestService.requestDownloadFile(url);
                System.out.println("파일 다운 : " + result);
                return result;
            }
        }
        return null;
    }

}
