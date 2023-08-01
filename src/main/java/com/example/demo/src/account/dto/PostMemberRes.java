package com.example.demo.src.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostMemberRes {
    private String jwtToken;
    private Long user_id;
}
