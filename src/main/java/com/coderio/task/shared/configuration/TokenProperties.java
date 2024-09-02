package com.coderio.task.shared.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "token")
public class TokenProperties {
    private String key;
    private String jwtCookieName;
    private String jwtRefreshCookieName;
    private Long expirationToken;
    private Long expirationRefreshTokem;

}
