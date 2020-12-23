package com.filecloud.authserver.client.endpoint;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.filecloud.authserver.client.dto.ForgotPasswordEmailDto;
import com.filecloud.authserver.response.Result;


@FeignClient(name = "EmailService", path = "api/v1/email")
public interface EmailServiceClient {

	@PostMapping("/email-forgot-password")
	Result<?> emailForgotPassword(ForgotPasswordEmailDto dto);

}
