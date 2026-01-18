package org.zhuyuqinlan.lemall.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.zhuyuqinlan.lemall.common.result.Result;
import org.zhuyuqinlan.lemall.common.result.ResultCode;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseEntity<Result<?>> bizException(BizException e) {
        log.error("业务异常: {}", e.getMessage(), e);
        HttpStatus status = toHttpStatus(e.getCode());
        return ResponseEntity.status(status).body(Result.fail(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<?>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数校验失败: {}", e.getMessage(), e);
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(ResultCode.VALIDATE_FAILED.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.fail(ResultCode.VALIDATE_FAILED.getCode(), message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<?>> constraintViolationException(ConstraintViolationException e) {
        log.error("参数校验失败: {}", e.getMessage(), e);
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(ResultCode.VALIDATE_FAILED.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.fail(ResultCode.VALIDATE_FAILED.getCode(), message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result<?>> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("参数类型不匹配: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.fail(ResultCode.VALIDATE_FAILED.getCode(), "参数类型错误"));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Result<?>> noHandlerFoundException(NoHandlerFoundException e) {
        log.error("接口不存在: {}", e.getRequestURL(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.fail(ResultCode.NOT_FOUND));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<?>> exception(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.fail(ResultCode.ERROR));
    }

    /**
     * 业务码转HTTP状态码
     */
    private HttpStatus toHttpStatus(int bizCode) {
        if (bizCode >= 50000) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (bizCode >= 40400) {
            return HttpStatus.NOT_FOUND;
        } else if (bizCode >= 40300) {
            return HttpStatus.FORBIDDEN;
        } else if (bizCode >= 40200) {
            return HttpStatus.CONFLICT;
        } else if (bizCode >= 40100) {
            return HttpStatus.UNAUTHORIZED;
        } else if (bizCode >= 40000) {
            return HttpStatus.BAD_REQUEST;
        } else if (bizCode >= 20000) {
            return HttpStatus.OK;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
