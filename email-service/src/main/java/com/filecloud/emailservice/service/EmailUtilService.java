package com.filecloud.emailservice.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.filecloud.emailservice.model.dto.ForgotPasswordEmailDto;
import com.filecloud.emailservice.model.dto.SharedDocumentEmailDto;


@Service
public class EmailUtilService {

	@Value("${spring.mail.username}")
	private String senderEmail;

	private final EmailSenderService emailSenderService;

	public EmailUtilService(EmailSenderService emailSenderService) {
		this.emailSenderService = emailSenderService;
	}

	@Async
	public Future<Boolean> sendMail(SharedDocumentEmailDto dto) throws MessagingException {
		String subject = "Document Shared";

		Map<String, Object> variables = new HashMap<>();
		variables.put("senderName", dto.getSenderName());
		variables.put("url", dto.getUrl());
		variables.put("expiryDays", dto.getExpiryDays());

		//        String body = "Hello World";
		//        Map<String, InputStreamSource> attachments = Collections.singletonMap("logo", new ClassPathResource("memorynotfound-logo.png"));
		Map<String, ClassPathResource> inlineResource = Collections.singletonMap("logo", new ClassPathResource("memorynotfound-logo.png"));

		String template = "email-share-document";
		boolean isHtml = true;

		emailSenderService.sendEmail(
				dto.getReceiverEmail(),
				senderEmail,
				subject,
				null,
				variables,
				null,
				inlineResource,
				template,
				isHtml);

		return new AsyncResult<>(true);
	}

	@Async
	public Future<Boolean> sendMail(ForgotPasswordEmailDto dto) throws MessagingException {
		String subject = "Forgot Password";

		Map<String, Object> variables = new HashMap<>();
		variables.put("name", dto.getName());
		variables.put("url", dto.getUrl());
		variables.put("expiryDays", dto.getExpiryDays());

		Map<String, ClassPathResource> inlineResource = Collections.singletonMap("logo", new ClassPathResource("memorynotfound-logo.png"));

		String template = "email-forgot-password";
		boolean isHtml = true;

		emailSenderService.sendEmail(
				dto.getReceiverEmail(),
				senderEmail,
				subject,
				null,
				variables,
				null,
				inlineResource,
				template,
				isHtml);

		return new AsyncResult<>(true);
	}
}
