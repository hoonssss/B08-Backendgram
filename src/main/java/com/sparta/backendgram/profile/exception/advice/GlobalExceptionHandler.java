package com.sparta.backendgram.profile.exception.advice;

import com.sparta.backendgram.profile.common.CommonErrorCode;
import com.sparta.backendgram.profile.exception.ErrorCode;
import com.sparta.backendgram.profile.exception.RestApiException;
import com.sparta.backendgram.profile.exception.response.ErrorResponse;
import com.sparta.backendgram.profile.exception.response.ErrorResponse.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleCustomException(RestApiException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
        WebRequest request) {
        log.warn("handleIllegalArgument", ex);
        CommonErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(ex, errorCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
            .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
            .code(errorCode.name())
            .message(errorCode.getMessage())
            .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
            .body(makeErrorResponse(e, errorCode));
    }

    private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {
        List<ValidationError> validationErrorList = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(ErrorResponse.ValidationError::of)
            .collect(Collectors.toList());

        return ErrorResponse.builder()
            .code(errorCode.name())
            .message(errorCode.getMessage())
            .errors(validationErrorList)
            .build();
    }

}
