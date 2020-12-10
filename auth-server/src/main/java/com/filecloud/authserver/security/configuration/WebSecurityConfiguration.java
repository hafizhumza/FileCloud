package com.filecloud.authserver.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.filecloud.authserver.properties.AuthServerProperties;
import com.filecloud.authserver.util.Util;


@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final AuthServerProperties authServerProperties;

	@Autowired
	public WebSecurityConfiguration(AuthServerProperties authServerProperties) {
		this.authServerProperties = authServerProperties;
	}

	@Bean
	protected AuthenticationManager getAuthenticationManager() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().contentSecurityPolicy("script-src 'self' ");
		http.csrf().disable();
	}

	@Override
	public void configure(WebSecurity web) {
		boolean securityEnabled = authServerProperties.security().isEnabled();

		if (!securityEnabled)
			web.ignoring().antMatchers("/**").anyRequest();

		if (securityEnabled && Util.isValidArray(authServerProperties.security().getIgnoredPaths()))
			web.ignoring().antMatchers(authServerProperties.security().getIgnoredPaths());
	}
}
