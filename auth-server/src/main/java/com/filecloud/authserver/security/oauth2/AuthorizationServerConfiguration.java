package com.filecloud.authserver.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfiguration implements AuthorizationServerConfigurer {

    @Value("${security.oauth2.client.access-token-validity-seconds:120}")
    private Integer accessTokenValiditySeconds;

    @Value("${security.oauth2.client.refresh-token-validity-seconds:300}")
    private Integer refreshTokenValiditySeconds;

    private final PasswordEncoder passwordEncoder;

    private final DataSource dataSource;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthorizationServerConfiguration(PasswordEncoder passwordEncoder, DataSource dataSource, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
        this.authenticationManager = authenticationManager;
    }

    @Bean
    TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(this.jdbcTokenStore());
        defaultTokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
        defaultTokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("permitAll()")
                .passwordEncoder(this.passwordEncoder) // For client secret.
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // For password grants
        endpoints.authenticationManager(authenticationManager);
        endpoints.tokenStore(this.jdbcTokenStore());
        endpoints.tokenServices(this.tokenServices());
    }
}
