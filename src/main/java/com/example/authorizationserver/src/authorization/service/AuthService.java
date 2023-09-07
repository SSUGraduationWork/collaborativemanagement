package com.example.authorizationserver.src.authorization.service;

import com.example.authorizationserver.config.BaseException;
import com.example.authorizationserver.src.entity.TeamMember;
import com.example.authorizationserver.src.repository.MemberRepository;
import com.example.authorizationserver.src.repository.TeamMemberRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.example.authorizationserver.config.BaseResponseStatus.SERVER_ERROR;

@Service
@AllArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    //check workspace permission
    public boolean checkWsPermission(Long teamId, Long userId, String role) {
        System.out.println(teamId);
        if (role.equals("student")){
            TeamMember member = teamMemberRepository.findByTeamIdAndUserId(teamId, userId)
                    .orElseThrow(IllegalAccessError::new);
            return true;

        } else if(role.equals("professor")) {    // role == "professor"

        } else {
            return false;
        }
        return true;
    }

    public JsonNode requestTo(String url, String method, JsonNode params) throws BaseException {
        try{

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity entity = new HttpEntity(params, headers);

            switch(method){
                case "GET": {
                    return restTemplate.getForEntity(url, JsonNode.class).getBody();
                }
                case "POST" : {
                    return restTemplate.postForEntity(url, entity, JsonNode.class).getBody();
                }
                case "DELETE" : {
                    return restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, JsonNode.class).getBody();
                }
                case "PUT" : {
                    return restTemplate.exchange(url, HttpMethod.PUT, entity, JsonNode.class).getBody();
                }
                default: {
                    throw new IllegalArgumentException("알 수 없는 method입니다.");
                }
            }

        } catch(Exception exception){
            System.out.println(exception.getMessage());
            throw new BaseException(SERVER_ERROR);
        }
    }
}
