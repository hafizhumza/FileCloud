package com.filecloud.emailservice.security.oauth2;

import com.filecloud.emailservice.constant.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${security.oauth2.resource.token-info-uri:null}")
    private String tokenInfoUri;

    @Value("${security.oauth2.client.client-id:null}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret:null}")
    private String clientSecret;

    @Value("${security.oauth2.resource.id:null}")
    private String resourceId;

    private final RestTemplate restTemplate;

    @Autowired
    public ResourceServerConfiguration(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .access("#oauth2.hasScope('" + ConstUtil.SCOPE_EMAIL + "')");
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenServices(tokenService());
        resources.resourceId(resourceId);
    }

    @Primary
    @Bean
    public AuthRemoteTokenServices tokenService() {
        AuthRemoteTokenServices tokenService = new AuthRemoteTokenServices();
        tokenService.setRestTemplate(restTemplate);
        tokenService.setCheckTokenEndpointUrl(tokenInfoUri);
        tokenService.setClientId(clientId);
        tokenService.setClientSecret(clientSecret);
        return tokenService;
    }
}
