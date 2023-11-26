package com.sparta.backendgram.profile.common;


import com.sparta.backendgram.profile.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 매개변수가 포함되어 있음"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 요소를 찾을 수 없음."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 문제");

    private final HttpStatus httpStatus;
    private final String message;

}
