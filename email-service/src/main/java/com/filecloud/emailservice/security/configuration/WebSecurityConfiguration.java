package com.filecloud.emailservice.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.filecloud.emailservice.properties.EmailServiceProperties;
import com.filecloud.emailservice.util.Util;


@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final EmailServiceProperties emailServiceProperties;

	@Autowired
	public WebSecurityConfiguration(EmailServiceProperties emailServiceProperties) {
		this.emailServiceProperties = emailServiceProperties;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().contentSecurityPolicy("script-src 'self' ");
		http.csrf().disable();
	}

	@Override
	public void configure(WebSecurity web) {
		if (Util.isValidArray(emailServiceProperties.security().getIgnoredPaths()))
			web.ignoring().antMatchers(emailServiceProperties.security().getIgnoredPaths());
	}
}
