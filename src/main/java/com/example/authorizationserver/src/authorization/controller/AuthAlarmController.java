package com.example.authorizationserver.src.authorization.controller;

import com.example.authorizationserver.config.BaseException;
import com.example.authorizationserver.src.authorization.service.RequestService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthAlarmController {

    private final RequestService requestService;
    @Value("${api-server.spring.domain}")
    private String domain;

    // 알람 조회 : [GET] /alarmList/view/{memberId}
    @GetMapping("/alarmList/view")
    public JsonNode AuthGetAlarms(HttpServletRequest request) throws BaseException{
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        if (userId != null && role != null) {
            System.out.println(domain);
            String url = domain + request.getRequestURI() + "/" + userId;
            JsonNode result = requestService.requestTo(url, "GET", null);
            return result;
        }
        return null;
    }
}
