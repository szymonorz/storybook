package pl.szyorz.storybook.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jwt")
public class JWTConfig {
    private String secret;
    private String prefix;
    private int expiresAfterDays;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getExpiresAfterDays() {
        return expiresAfterDays;
    }

    public void setExpiresAfterDays(int expiresAfterDays) {
        this.expiresAfterDays = expiresAfterDays;
    }
}
