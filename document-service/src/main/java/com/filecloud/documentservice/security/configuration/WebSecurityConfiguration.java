package com.filecloud.documentservice.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.filecloud.documentservice.properties.DocumentServiceProperties;
import com.filecloud.documentservice.util.Util;


@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final DocumentServiceProperties documentServiceProperties;

	@Autowired
	public WebSecurityConfiguration(DocumentServiceProperties documentServiceProperties) {
		this.documentServiceProperties = documentServiceProperties;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().contentSecurityPolicy("script-src 'self' ");
		http.csrf().disable();
	}

	@Override
	public void configure(WebSecurity web) {
		if (Util.isValidArray(documentServiceProperties.security().getIgnoredPaths()))
			web.ignoring().antMatchers(documentServiceProperties.security().getIgnoredPaths());
	}
}
