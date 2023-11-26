package com.sparta.backendgram.profile.exception;

public class PasswordIsNotMatchException extends RestApiException {

    public PasswordIsNotMatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
