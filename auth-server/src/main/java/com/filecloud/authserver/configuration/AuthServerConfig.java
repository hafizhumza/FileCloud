package com.filecloud.authserver.configuration;

import com.filecloud.authserver.properties.AuthServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AuthServerProperties.class)
public class AuthServerConfig {
}
