package com.filecloud.documentservice.service;

import com.filecloud.documentservice.model.dto.EmailSharedDocumentUrlDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;


@Service
public class EmailService extends BaseService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Async
    public Future<Boolean> sendMail(EmailSharedDocumentUrlDto emailSharedDocumentUrlDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            helper.setFrom(senderEmail);
            helper.setTo(emailSharedDocumentUrlDto.getToEmail());
            helper.setSubject("Hi!");
            helper.setText("URL: " + emailSharedDocumentUrlDto.getUrl());

            mailSender.send(message);
            return new AsyncResult<>(true);
        } catch (MessagingException e) {
            error("Error while sending email");
        }

        return new AsyncResult<>(false);
    }

}
