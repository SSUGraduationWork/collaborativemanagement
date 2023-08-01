package com.example.demo.config;

import com.example.demo.src.account.dto.JwtDto;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;    //자바 객체를 json으로 serialization

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        boolean check = checkAnnotation(handler, NoAuth.class);
        if(check) return true;

        try{

            JwtDto jwtDto = jwtService.getUserInfo();
            Long userIdByJwt = jwtDto.getId();
            String userRoleByJwt = jwtDto.getRole();
            request.setAttribute("userId", userIdByJwt);
            request.setAttribute("role", userRoleByJwt);

        } catch(BaseException exception){
            String requestURI = request.getRequestURI();

            Map<String, String> map = new HashMap<>();
            //redirectURI는 로그인 후 다시 원래 페이지로 돌아가기 위함이다.
            map.put("requestURI", "/accounts/oauth2/signup?redirectURI="+requestURI);
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            response.getWriter().write(json);

            return false;
        }
        return true;
    }

    private boolean checkAnnotation(Object handler, Class cls){
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if(handlerMethod.getMethodAnnotation(cls)!=null){   //해당 annotation이 존재하면 true
            return true;
        }
        return false;
    }
}
