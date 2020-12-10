package com.filecloud.authserver.security.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import com.filecloud.authserver.constant.ConstUtil;


/**
 * In order to secure Controllers endpoints with oauth2 we need configure Resource Server.
 */
@EnableResourceServer
@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/api/v1/admin/**")
				.access("#oauth2.hasScope('" + ConstUtil.SCOPE_WRITE + "')");
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId("u_437287");
	}

	/*@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
	    resources.authenticationEntryPoint(authenticationEntryPoint());
	}*/

	/*@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
	    return new AuthRequestEntryPoint();
	}*/
}
