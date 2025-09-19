package org.zhuyuqinlan.lemall.business.portal.sso.service;



public interface UmsMemberCacheService {
    /**
     * 设置验证码
     */
    void setAuthCode(String email, String authCode);

    /**
     * 获取验证码
     */
    String getAuthCode(String email);

    /**
     * 检查邮箱是否可以获取验证码（频率限制）
     * @param email 邮箱
     * @return true 表示可以发送，false 表示在限制时间内不允许发送
     */
    boolean canSendAuthCode(String email);
}
