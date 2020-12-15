package com.filecloud.adminservice.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.filecloud.adminservice.properties.AdminServiceProperties;
import com.filecloud.adminservice.util.Util;


@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final AdminServiceProperties adminServiceProperties;

	@Autowired
	public WebSecurityConfiguration(AdminServiceProperties adminServiceProperties) {
		this.adminServiceProperties = adminServiceProperties;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().contentSecurityPolicy("script-src 'self' ");
		http.csrf().disable();
	}

	@Override
	public void configure(WebSecurity web) {
		if (Util.isValidArray(adminServiceProperties.security().getIgnoredPaths()))
			web.ignoring().antMatchers(adminServiceProperties.security().getIgnoredPaths());
	}
}
