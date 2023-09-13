package com.example.authorizationserver.src.authorization.service;

import com.example.authorizationserver.config.BaseException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import static com.example.authorizationserver.config.BaseResponseStatus.SERVER_ERROR;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();


    public JsonNode requestTo(String url, String method, JsonNode params) throws BaseException {
        try{

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
                    return restTemplate.exchange(url, HttpMethod.DELETE, entity, JsonNode.class).getBody();
                }
                case "PUT" : {
                    return restTemplate.exchange(url, HttpMethod.PUT, entity, JsonNode.class).getBody();
                }
                case "POSTReturnVoid" : {
                    ResponseEntity<Void> responseEntity = restTemplate.postForEntity(url, entity, Void.class);
                    System.out.println("POSTReturnVoid : " + responseEntity);
                    return null;
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

    public ResponseEntity<Void> requestFormData(String url, String method, String title, String content, MultipartFile[] files) throws BaseException {
        try {

            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            if(files != null){
                for (MultipartFile file : files) {
                    body.add("files", file.getResource());
                }
            }
            body.add("title", title);
            body.add("content", content);
            HttpEntity entity = new HttpEntity(body, headers);
            System.out.println("body : " + body);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);

            System.out.println(responseEntity.getStatusCode());

            return responseEntity;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new BaseException(SERVER_ERROR);
        }
    }
    public JsonNode requestModFormData(String url, String method, String title, String content, MultipartFile[] files) throws BaseException {
        try {

            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            if(files != null){
                for (MultipartFile file : files) {
                    body.add("files", file.getResource());
                }
            }
            body.add("title", title);
            body.add("content", content);
            HttpEntity entity = new HttpEntity(body, headers);
            System.out.println("body : " + body);

            return restTemplate.exchange(url, HttpMethod.POST, entity, JsonNode.class).getBody();

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new BaseException(SERVER_ERROR);
        }
    }

    public ResponseEntity<Resource> requestDownloadFile(String url) throws BaseException{

        try{
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            HttpEntity entity = new HttpEntity(null, headers);

            return restTemplate.exchange(url, HttpMethod.GET,  entity, Resource.class);
        } catch(Exception exception){
            System.out.println(exception.getMessage());
            throw new BaseException(SERVER_ERROR);
        }
    }

}
