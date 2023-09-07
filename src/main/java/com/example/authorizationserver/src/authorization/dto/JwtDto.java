package com.example.authorizationserver.src.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtDto {
    private Long id;
    private String role;
}
