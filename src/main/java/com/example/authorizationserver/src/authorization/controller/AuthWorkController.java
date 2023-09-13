package com.example.authorizationserver.src.authorization.controller;

import com.example.authorizationserver.config.BaseException;
import com.example.authorizationserver.src.authorization.service.AuthService;
import com.example.authorizationserver.src.authorization.service.RequestService;
import com.example.authorizationserver.utils.JwtService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/work")
@RequiredArgsConstructor
public class AuthWorkController {
    private final AuthService authService;
    private final RequestService requestService;
    @Value("${api-server.node.domain}")
    private String domain;
    @GetMapping("/{teamId}")
    public JsonNode authGetWorks(HttpServletRequest request,
                                 @PathVariable(value = "teamId") Long teamId) throws BaseException {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        if (userId != null && role != null) {
            Boolean permission = authService.checkViewPermission(teamId, userId, role);
            if (permission) {
                String url = domain + "work/" + teamId;
                JsonNode result =  requestService.requestTo(url, "GET", null);
                System.out.println(result);
                return result;
            }
        }
        return null;
    }

    @PostMapping("/{teamId}")
    public JsonNode authPostWork(HttpServletRequest request,
                                 @PathVariable(value = "teamId") Long teamId) throws BaseException{
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        if (userId != null && role != null) {
            Boolean permission = authService.checkPostPermission(teamId, userId, role);
            if (permission) {
                String url = domain + "work/" + teamId;
                return requestService.requestTo(url, "POST", null);
            }
        }
        return null;
    }

    @GetMapping("/detail/{teamId}/{workId}")
    public JsonNode authGetWork(HttpServletRequest request,
                                @PathVariable(value="teamId") Long teamId,
                                @PathVariable(value="workId") Long workId) throws BaseException{
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        if (userId != null && role != null) {
            Boolean permission = authService.checkViewPermission(teamId, userId, role);
            if (permission) {
                String url = domain + "work/detail/" + teamId +"/"+ workId;
                return requestService.requestTo(url, "GET", null);
            }
        }
        return null;
    }

    @DeleteMapping("/{teamId}/{workId}")
    public JsonNode authDeleteWork(HttpServletRequest request,
                                   @PathVariable(value="teamId") Long teamId,
                                   @PathVariable(value="workId") Long workId) throws BaseException{
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        if (userId != null && role != null) {
            Boolean permission = authService.checkPostPermission(teamId, userId, role);
            if (permission) {
                String url = domain + "work/" + +teamId +"/"+ workId;
                return requestService.requestTo(url, "DELETE", null);
            }
        }
        return null;
    }

    @PostMapping("/{teamId}/{workId}/{updateValue}")
    public JsonNode authGetWork(HttpServletRequest request,
                                @PathVariable(value="teamId") Long teamId,
                                @PathVariable(value="workId") Long workId,
                                @PathVariable(value="updateValue") String updateValue,
                                @RequestBody JsonNode jsonData) throws BaseException{
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        if (userId != null && role != null) {
            Boolean permission = authService.checkPostPermission(teamId, userId, role);
            if (permission) {
                String url = domain + "work/" + teamId +"/"+ workId +"/"+ updateValue;
                return requestService.requestTo(url, "POST", jsonData);
            }
        }
        return null;
    }

}
