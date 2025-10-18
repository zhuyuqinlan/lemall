package org.zhuyuqinlan.lemall;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.zhuyuqinlan.lemall.config.BusinessModuleConfig;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@EnableTransactionManagement
@EnableCaching
@Import(BusinessModuleConfig.class)
@SpringBootApplication(scanBasePackages = {
        "org.zhuyuqinlan.lemall.common",
        "org.zhuyuqinlan.lemall.auth"
})
@MapperScan("org.zhuyuqinlan.lemall.common.**.mapper")
public class Application {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(Application.class, args);
        Environment env = application.getEnvironment();

        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port", "8080");
        String path = env.getProperty("server.servlet.context-path", "");

        log.info("\n----------------------------------------------------------\n\tlemall商城项目启动成功，访问地址:\n\t本地: \t\thttp://localhost:{}{}/\n\t外部: \t\thttp://{}:{}{}/\n\tAPI文档: \thttp://{}:{}{}/doc.html\n----------------------------------------------------------", port, path, ip, port, path, ip, port, path);
    }
}
