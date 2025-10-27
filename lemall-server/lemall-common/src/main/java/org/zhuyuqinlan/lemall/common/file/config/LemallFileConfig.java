package org.zhuyuqinlan.lemall.common.file.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "file")
public class LemallFileConfig {

    private List<String> mimeAllow;

    public Map<String, List<String>> toMimeAllowMap() {
        Map<String, List<String>> map = new HashMap<>();
        for (String item : mimeAllow) {
            String[] split = item.split(":");
            if (split.length != 2) continue;
            String mime = split[0];
            List<String> exts = Arrays.asList(split[1].split(","));
            map.put(mime, exts);
        }
        return map;
    }
}

