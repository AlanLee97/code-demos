package top.alanlee.template.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置属性类
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties{
    private long expire;
    private String secret;

    public JwtConfigProperties() {
    }

    public JwtConfigProperties(long expire, String secret) {
        this.expire = expire;
        this.secret = secret;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "JwtConfigProperties{" +
                "expire=" + expire +
                ", secret='" + secret + '\'' +
                '}';
    }
}