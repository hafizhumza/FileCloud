package com.filecloud.emailservice.service;

import com.filecloud.emailservice.model.dto.EmailSharedDocumentUrlDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class EmailUtilService {

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final EmailSenderService emailSenderService;

    public EmailUtilService(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @Async
    public Future<Boolean> sendMail(EmailSharedDocumentUrlDto dto) throws MessagingException {
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
                isHtml
        );

        return new AsyncResult<>(true);
    }
}
