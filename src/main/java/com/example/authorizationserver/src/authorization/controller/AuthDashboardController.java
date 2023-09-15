package com.example.authorizationserver.src.authorization.controller;

import com.example.authorizationserver.config.BaseException;
import com.example.authorizationserver.config.BaseResponse;
import com.example.authorizationserver.config.BaseResponseStatus;
import com.example.authorizationserver.src.account.dto.JwtDto;
import com.example.authorizationserver.src.authorization.service.AuthService;
import com.example.authorizationserver.src.authorization.service.RequestService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/dashboard")
@RequiredArgsConstructor
public class AuthDashboardController {
    private final AuthService authService;
    private final RequestService requestService;
    @Value("${api-server.spring.domain}")
    private String serverDomain;
    @Value("${front.domain}")
    private String frontDomain;

    @GetMapping("/mypage")
    public BaseResponse<JwtDto> redirectToDashboard(HttpServletRequest request, HttpServletResponse response) throws BaseException{
        try{
            Long userId = (Long) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");
            if(userId != null && role != null){
                JwtDto userInfo = new JwtDto(userId, role);
                return new BaseResponse<>(userInfo);
            } else{
                throw new BaseException(BaseResponseStatus.FORBIDDEN);
            }
        }catch(Exception exception){
            System.out.println(exception.getMessage());
            throw new BaseException(BaseResponseStatus.SERVER_ERROR);
        }
    }

    //Dashboard 조회(교수님) : [GET] /dashboard/projects/{professorId}
    //Dashboard 조회(학생) : [GET] /dashboard/teamsByStu/{id}
    @GetMapping(value = "/{role}/{id}")
    public JsonNode authGetStudentDashboard(HttpServletRequest request,
                                            @PathVariable("role") String roleType,
                                            @PathVariable("id") Long id) throws BaseException{
        try{
            Long userId = (Long) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");
            if (userId.equals(id) && role.equals(roleType)){
                String url = "";
                if(roleType.equals("professor")){
                    url = serverDomain + "dashboard/projects/" + userId;
                } else if(roleType.equals("student")){
                    url = serverDomain + "dashboard/teamsByStu/" + userId;
                } else{
                    throw new BaseException(BaseResponseStatus.FORBIDDEN);
                }
                return requestService.requestTo(url, "GET", null);
            } else{
                throw new BaseException(BaseResponseStatus.INVALID_USER_JWT);
            }

        }catch(Exception exception){
            System.out.println(exception.getMessage());
            throw new BaseException(BaseResponseStatus.SERVER_ERROR);
        }
    }


    //팀 수정 : [POST] /dashboard/teams
    @PostMapping(value = "/teams")
    public JsonNode authModTeam(HttpServletRequest request, @RequestBody JsonNode jsonData) throws BaseException{
        try{
            Long userId = (Long) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");
            Long teamId = jsonData.get("teamId").asLong();
            if(userId != null && role != null){
                Boolean permission = authService.checkViewPermission(teamId, userId, role);
                if(permission){
                    String url = serverDomain +  request.getRequestURI();
                    JsonNode result = requestService.requestTo(url, "POST", jsonData);
                    return result;
                }
                else{
                    throw new BaseException(BaseResponseStatus.FORBIDDEN);
                }
            } else{
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
        }catch(Exception exception){
            System.out.println(exception.getMessage());
            throw new BaseException(BaseResponseStatus.SERVER_ERROR);
        }
    }

    //프로젝트 내에 팀 조회 : [GET] /dashboard/teamsByPro/{projectId}
    @GetMapping(value="/teamsByPro/{projectId}")
    public JsonNode authGetTeams(HttpServletRequest request,
                                 @PathVariable("projectId")Long projectId) throws BaseException{
        try{
            Long userId = (Long) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");
            if(userId != null && role != null){
                String url = serverDomain + request.getRequestURI();
                return requestService.requestTo(url, "GET", null);
            } else{
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
        } catch(Exception exception){
            System.out.println(exception.getMessage());
            throw new BaseException(BaseResponseStatus.SERVER_ERROR);
        }
    }

    //팀 생성(일단은 학생만 가능) : [POST] /dashboard/teams/{studentId}
    @PostMapping(value = "/team")
    public JsonNode authPostTeam(HttpServletRequest request, @RequestBody JsonNode jsonData) throws BaseException{
        try{
            Long userId = (Long) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");
            if(userId != null && role.equals("student")){
                String url = serverDomain + request.getRequestURI() + "s/" + userId;
                return requestService.requestTo(url, "POST", jsonData);
            } else{
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new BaseException(BaseResponseStatus.SERVER_ERROR);
        }
    }

    //팀 삭제 (교수님만 가능) : [DELETE] /dashboard/teams/{teamId}
    @DeleteMapping(value = "/teams/{teamId}")
    public JsonNode authDeleteTeam(HttpServletRequest request,
                                   @PathVariable("teamId") Long teamId) throws BaseException{
        try{
            Long userId = (Long) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");

            if(userId != null && role.equals("professor")){
                Boolean permission = authService.checkDeleteTeam(userId, teamId);
                if(permission){
                    String url = serverDomain + request.getRequestURI();
                    return requestService.requestTo(url, "DELETE", null);
                } else{
                    throw new BaseException(BaseResponseStatus.FORBIDDEN);
                }
            } else{
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
        } catch(Exception exception) {
            System.out.println(exception.getMessage());
            throw new BaseException(BaseResponseStatus.SERVER_ERROR);
        }
    }

    //프로젝트 생성(교수님) : [POST] /dashboard/projects
    @PostMapping(value = "/projects")
    public JsonNode authCreateProject (HttpServletRequest request, @RequestBody JsonNode data) throws BaseException{
        try{
            Long userId = (Long) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");
            Long professorId = data.get("professorId").asLong();
            if (userId.equals(professorId) && role.equals("professor")){
                String url = serverDomain + request.getRequestURI();
                return requestService.requestTo(url, "POST", data);
            } else{
                throw new BaseException(BaseResponseStatus.FORBIDDEN);
            }
        } catch(Exception exception){
            System.out.println(exception.getMessage());
            throw new BaseException(BaseResponseStatus.SERVER_ERROR);
        }
    }

    //프로젝트 수정(교수님) : [POST] /dashboard/project
    @PostMapping(value = "/project")
    public JsonNode authModProject (HttpServletRequest request, @RequestBody JsonNode data) throws BaseException{
        try{
            Long userId = (Long) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");
            Long professorId = data.get("professorId").asLong();
            if (userId.equals(professorId) && role.equals("professor")){
                String url = serverDomain + request.getRequestURI();
                return requestService.requestTo(url, "POST", data);
            } else{
                throw new BaseException(BaseResponseStatus.FORBIDDEN);
            }
        } catch(Exception exception){
            System.out.println(exception.getMessage());
            throw new BaseException(BaseResponseStatus.SERVER_ERROR);
        }
    }

    //프로젝트 삭제(교수님) : [DELETE] /dashboard/projects/{projectId}
    @DeleteMapping(value = "/projects/{projectId}")
    public JsonNode authDeleteProject(HttpServletRequest request,
                                      @PathVariable("projectId") Long projectId) throws BaseException{
        try{
            Long userId = (Long) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");

            if(userId != null && role.equals("professor")){
                Boolean permission = authService.checkProfessorProject(userId, projectId);
                if(permission) {
                    String url = serverDomain + request.getRequestURI();
                    return requestService.requestTo(url, "DELETE", null);
                }
                throw new BaseException(BaseResponseStatus.FORBIDDEN);
            } else{
                throw new BaseException(BaseResponseStatus.FORBIDDEN);
            }
        } catch(Exception exception){
            System.out.println(exception.getMessage());
            throw new BaseException(BaseResponseStatus.SERVER_ERROR);
        }
    }

}
