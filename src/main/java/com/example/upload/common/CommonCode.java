package com.example.extendablechattingbe.common.code;

import com.example.extendablechattingbe.common.response.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonCode implements ResponseCode {

    GOOD_REQUEST(1000, "올바른 요청입니다."),
    BAD_REQUEST(1001, "잘못된 요청입니다."),
    ILLEGAL_REQUEST(1002, "잘못된 데이터가 포함된 요청입니다."),
    VALIDATION_FAILURE(1003, "입력값 검증이 실패하였습니다."),
    INTERNAL_SERVER_ERROR(1004, "서버 내부에 문제가 생겼습니다."),
    ;

    private final int code;
    private final String message;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
