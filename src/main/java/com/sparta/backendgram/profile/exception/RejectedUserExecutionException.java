package com.sparta.backendgram.profile.exception;

public class RejectedUserExecutionException extends RestApiException {

    public RejectedUserExecutionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
