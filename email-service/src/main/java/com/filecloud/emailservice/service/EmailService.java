package com.filecloud.emailservice.service;

import java.util.concurrent.ExecutionException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.filecloud.emailservice.model.db.Email;
import com.filecloud.emailservice.model.dto.EmailSharedDocumentDto;
import com.filecloud.emailservice.repository.EmailRepository;
import com.filecloud.emailservice.security.dto.UserSession;
import com.filecloud.emailservice.security.util.AuthUtil;


@Service
public class EmailService extends BaseService {

	private final EmailRepository emailRepository;

	private final EmailUtilService emailUtilService;

	@Autowired
	public EmailService(EmailRepository emailRepository, EmailUtilService emailUtilService) {
		this.emailRepository = emailRepository;
		this.emailUtilService = emailUtilService;
	}

	public void saveAndSend(EmailSharedDocumentDto dto) {
		UserSession session = AuthUtil.getCurrentLoggedInUser();
		boolean sent = false;

		try {
			sent = emailUtilService.sendMail(dto).get();
		} catch (MessagingException | InterruptedException | ExecutionException e) {
			error(e);
		}

		Email email = new Email();
		email.setSenderEmail(session.getEmail());
		email.setReceiverEmail(dto.getReceiverEmail());
		email.setSenderName(dto.getSenderName());
		email.setSenderId(session.getUserId());
		email.setDescription(String.format("Share document URL: %s. URL valid for %s days.", dto.getUrl(), dto.getExpiryDays()));
		email.setWasSent(sent);

		emailRepository.save(email);
	}
}
