package org.nanguo.lemall.common.util.response;

import lombok.Getter;

/**
 * 预定义状态码枚举
 */
@Getter
public enum ResultCode{
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败");
    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
