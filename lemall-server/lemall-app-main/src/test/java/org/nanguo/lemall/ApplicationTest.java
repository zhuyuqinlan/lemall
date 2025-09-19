package org.zhuyuqinlan.lemall;

import cn.hutool.crypto.digest.BCrypt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTest {

    @Test
    public void getAdminPasswd() {
        // 原始密码
        String password = "admin";

        // 生成加密后的密码
        String hashedPassword = BCrypt.hashpw(password);

        System.out.println("加密后的密码: " + hashedPassword);
    }
}
