package org.zhuyuqinlan.lemall;

import cn.hutool.crypto.digest.BCrypt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.zhuyuqinlan.lemall.common.service.RedisService;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class ApplicationTest {

    private final RedisService redisService;

    public ApplicationTest(RedisService redisService) {
        this.redisService = redisService;
    }

    @Test
    public void getAdminPasswd() {
        // 原始密码
        String password = "admin";

        // 生成加密后的密码
        String hashedPassword = BCrypt.hashpw(password);

        System.out.println("加密后的密码: " + hashedPassword);
    }

    @Test
    public void testRedisSet() {
        String key = "test:key";
        String value = "hello redis";

        // 设置 key
        redisService.set(key, value);

        // 获取 key
        String result = redisService.get(key);
        System.out.println("Redis 中获取的值: " + result);

        // 简单断言
        assert value.equals(result);
    }

}
