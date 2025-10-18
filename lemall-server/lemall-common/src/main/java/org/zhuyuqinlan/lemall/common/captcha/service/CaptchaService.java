package org.zhuyuqinlan.lemall.common.captcha.service;

/**
 * 通用验证码操作接口
 */
public interface CaptchaService {

    /**
     * 设置验证码（调用服务去获取验证码）
     * @param target 接收目标，例如邮箱或手机号
     * @param type 验证码类型，例如 "EMAIL"、"SMS" 等
     * @param expireSeconds 验证码过期时间（秒）
     * @param intervalSeconds 两次发送最小间隔时间（秒）
     * @return 验证码
     */
    String setAuthCode(String target, String type, long expireSeconds, long intervalSeconds);

    /**
     * 获取验证码
     * @param target 接收目标
     * @param type 验证码类型
     * @return 验证码
     */
    String getAuthCode(String target, String type);

    /**
     * 判断是否可以再次发送验证码
     * @param target 接收目标
     * @param type 验证码类型
     * @return 是否允许发送
     */
    boolean canSendAuthCode(String target, String type);
}

