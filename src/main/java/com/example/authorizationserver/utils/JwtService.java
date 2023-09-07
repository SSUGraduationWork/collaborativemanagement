package com.example.authorizationserver.utils;

import com.example.authorizationserver.config.BaseException;
import com.example.authorizationserver.src.authorization.dto.JwtDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.util.Date;

import static com.example.authorizationserver.config.BaseResponseStatus.EMPTY_JWT;
import static com.example.authorizationserver.config.BaseResponseStatus.INVALID_JWT;

@Service
public class JwtService {
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    /**
     * JWT 생성
     * @param userId
     * @return String
     */
    public String createJwt(Long userId, String role){
        Date now = new Date();
        String jwt =  Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
                .signWith(key)  //signature 부분
                .compact();
        return jwt;
    }

    /**
     * Header에서 Authorization으로 JWT 추출
     * @return String
     */
    public String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            System.out.println("token: " + token);
        }

        return token;
    }

    /**
     * JWT에서 userId, role 추출
     * @return JwtDto
     * @throws BaseException
     */
    public JwtDto getUserInfo() throws BaseException{
        //1. JWT 추출
        String accessToken = getJwt();
        if(accessToken == null || accessToken.length() == 0){
            System.out.println(EMPTY_JWT.getMessage());
            throw new BaseException(EMPTY_JWT);
        }

        //2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(accessToken);
        } catch(ExpiredJwtException e){
            //토큰이 만료된 경우에 대한 처리
            System.out.println(e.getMessage());
            throw new BaseException(INVALID_JWT);
        } catch(MalformedJwtException e){
            //토큰이 유효하지 않거나 변조된 경우에 대한 처리
            System.out.println(e.getMessage());
            throw new BaseException(INVALID_JWT);
        } catch(SignatureException e){
            //서명이 올바르지 않은 경우
            System.out.println(e.getMessage());
            throw new BaseException(INVALID_JWT);
        } catch(Exception e){
            System.out.println(e.getMessage());
            throw new BaseException(INVALID_JWT);
        }

        //3. userId, role 추출 후 반환
        Long userId = claims.getBody().get("userId", Long.class);
        String role = claims.getBody().get("role", String.class);
        JwtDto jwtDto = new JwtDto(userId, role);
        return jwtDto;
    }

}
