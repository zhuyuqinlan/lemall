package org.nanguo.lemall.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.nanguo.lemall.common.util.response.BizException;
import org.nanguo.lemall.common.util.response.Result;
import org.nanguo.lemall.common.util.response.ResultCode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handle(MethodArgumentNotValidException e) {
        log.error(e.getMessage(),e);
        return StringUtils.hasText(e.getMessage()) ? Result.fail(e.getMessage()) : Result.fail(ResultCode.VALIDATE_FAILED);
    }

    @ExceptionHandler(value = BizException.class)
    public Result<?> BizException(BizException e) {
        log.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public Result<?> MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public Result<?> exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }
}
