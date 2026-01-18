package org.zhuyuqinlan.lemall.common.exception;

import lombok.Getter;
import org.zhuyuqinlan.lemall.common.result.ResultCode;

@Getter
public class BizException extends RuntimeException {

    private final int code;
    private final String message;

    public BizException(String message) {
        super(message);
        this.code = ResultCode.FAILED.getCode();
        this.message = message;
    }

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public BizException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
        this.message = message;
    }
}
