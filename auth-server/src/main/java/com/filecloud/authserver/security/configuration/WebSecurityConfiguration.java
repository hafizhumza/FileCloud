package com.filecloud.authserver.security.configuration;

import com.filecloud.authserver.properties.AppProperties;
import com.filecloud.authserver.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AppProperties appProperties;

    @Autowired
    public WebSecurityConfiguration(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    protected AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    /*@Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AuthFailureRequestHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AuthAccessDeniedRequestHandler();
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().contentSecurityPolicy("script-src 'self' ");
        http.csrf().disable();
//        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
//                .failureHandler(authenticationFailureHandler());
    }

    @Override
    public void configure(WebSecurity web) {
        boolean securityEnabled = appProperties.security().isEnabled();

        if (!securityEnabled)
            web.ignoring().antMatchers("/**").anyRequest();

        if (securityEnabled && Util.isValidArray(appProperties.security().getIgnoredPaths()))
            web.ignoring().antMatchers(appProperties.security().getIgnoredPaths());
    }
}
