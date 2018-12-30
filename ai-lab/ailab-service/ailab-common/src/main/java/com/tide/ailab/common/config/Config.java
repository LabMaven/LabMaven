package com.tide.ailab.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 配置类
 * @author User
 */
@Component
@PropertySource(value = "classpath:config.properties") //配置文件路径
public class Config {

    /**
     * token密钥
     */
    @Value("${token.base64Security}")
    private String base64Security;

    /**
     * token失效时长
     */
    @Value("${token.expiry.time}")
    private long expiryTime;

    public String getBase64Security() {
        return base64Security;
    }

    public void setBase64Security(String base64Security) {
        this.base64Security = base64Security;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

}
