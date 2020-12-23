package com.filecloud.emailservice.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filecloud.emailservice.model.dto.ForgotPasswordEmailDto;
import com.filecloud.emailservice.model.dto.SharedDocumentEmailDto;
import com.filecloud.emailservice.response.Response;
import com.filecloud.emailservice.response.Result;
import com.filecloud.emailservice.service.EmailService;


@Transactional(readOnly = true)
@RequestMapping("api/v1/email")
@RestController
public class EmailController extends BaseController {

	private final EmailService emailService;

	@Autowired
	public EmailController(EmailService emailService) {
		this.emailService = emailService;
	}

	@Transactional
	@PostMapping("/email-shared-document")
	public Result<?> emailSharedDocumentUrl(@RequestBody @Valid SharedDocumentEmailDto dto) {
		emailService.saveAndSend(dto);
		return sendSuccessResponse(Response.Status.ALL_OK, "Email has been sent successfully");
	}

	@Transactional
	@PostMapping("/email-forgot-password")
	public Result<?> emailForgotPassword(@RequestBody @Valid ForgotPasswordEmailDto dto) {
		emailService.saveAndSend(dto);
		return sendSuccessResponse(Response.Status.ALL_OK, "Email has been sent successfully");
	}
}
