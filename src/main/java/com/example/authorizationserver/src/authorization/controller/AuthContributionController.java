package com.example.authorizationserver.src.authorization.controller;

import com.example.authorizationserver.config.BaseException;
import com.example.authorizationserver.src.authorization.service.AuthService;
import com.example.authorizationserver.src.authorization.service.RequestService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/contribution")
@RequiredArgsConstructor
public class AuthContributionController {
    private final AuthService authService;
    private final RequestService requestService;

    @Value("${api-server.node.domain}")
    private String domain;

    @GetMapping("/{teamId}")
    public JsonNode authGetContribution(HttpServletRequest request, @PathVariable("teamId") Long teamId) throws BaseException {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        if (userId != null && role != null) {
            Boolean permission = authService.checkViewPermission(teamId, userId, role);
            if (permission) {
                String url = domain + "contribution/" + teamId;
                JsonNode result =  requestService.requestTo(url, "GET", null);
                System.out.println(result);
                return result;
            }
        }
        return null;
    }
}
