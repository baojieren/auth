package com.gmsj.base;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author baojieren
 * @date 2020/4/17 13:17
 */
@Data
@Component
@ConfigurationProperties(prefix = "appconfig")
public class YmlConfig {
    @Value("${spring.profiles.active}")
    public String debug;

    /**
     * 小程序appId
     */
    public String appId;

    /**
     * 小程序密钥
     */
    public String secret;
}
