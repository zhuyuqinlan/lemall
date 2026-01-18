package org.zhuyuqinlan.lemall.common.result;

import lombok.Getter;

/**
 * 业务状态码枚举（5位，避免与HTTP状态码冲突）
 * <p>
 * 规范：2xxxx-成功 4xxxx-客户端错误 5xxxx-服务端错误
 */
@Getter
public enum ResultCode {
    // ========== 成功 ==========
    SUCCESS(20000, "操作成功"),

    // ========== 客户端错误 4xxxx ==========
    VALIDATE_FAILED(40001, "参数校验失败"),
    UNAUTHORIZED(40002, "未登录或登录已过期"),
    FORBIDDEN(40003, "无权限访问"),
    NOT_FOUND(40004, "资源不存在"),
    METHOD_NOT_ALLOWED(40005, "请求方法不支持"),
    MEDIA_TYPE_NOT_SUPPORTED(40006, "请求媒体类型不支持"),

    // 业务相关
    USER_NOT_EXIST(40101, "用户不存在"),
    USER_PASSWORD_ERROR(40102, "用户名或密码错误"),
    USER_ACCOUNT_DISABLED(40103, "账号已被禁用"),
    PHONE_FORMAT_ERROR(40104, "手机号格式错误"),
    EMAIL_FORMAT_ERROR(40105, "邮箱格式错误"),
    VERIFICATION_CODE_ERROR(40106, "验证码错误或已过期"),
    CAPTCHA_ERROR(40107, "图形验证码错误"),

    // 商品相关
    PRODUCT_OUT_OF_STOCK(40201, "商品库存不足"),
    PRODUCT_NOT_EXIST(40202, "商品不存在"),
    PRODUCT_SOLD_OUT(40203, "商品已下架"),

    // 订单相关
    ORDER_NOT_EXIST(40301, "订单不存在"),
    ORDER_STATUS_ERROR(40302, "订单状态异常"),
    ORDER_CANNOT_CANCEL(40303, "订单当前状态不可取消"),
    ORDER_CANNOT_PAY(40304, "订单当前状态不可支付"),

    // 优惠券相关
    COUPON_NOT_EXIST(40401, "优惠券不存在"),
    COUPON_EXPIRED(40402, "优惠券已过期"),
    COUPON_USED(40403, "优惠券已使用"),
    COUPON_NOT_AVAILABLE(40404, "优惠券不可用"),
    COUPON_RECEIVE_LIMIT(40405, "优惠券领取次数已达上限"),

    // ========== 服务端错误 5xxxx ==========
    FAILED(50000, "操作失败"),
    ERROR(50001, "系统内部错误"),
    SERVICE_UNAVAILABLE(50002, "服务暂不可用"),
    DATABASE_ERROR(50003, "数据库操作失败"),
    REDIS_ERROR(50004, "缓存服务异常"),
    FILE_UPLOAD_ERROR(50005, "文件上传失败"),
    FILE_DOWNLOAD_ERROR(50006, "文件下载失败"),
    THIRD_PARTY_ERROR(50007, "第三方服务调用失败");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
