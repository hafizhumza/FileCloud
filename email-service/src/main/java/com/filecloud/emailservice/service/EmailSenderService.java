package com.filecloud.emailservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Service
public class EmailSenderService extends BaseService {

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    @Autowired
    public EmailSenderService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * @param to          sender email address
     * @param from        receiver email address
     * @param subject     of the email
     * @param body        will be sent if you want to send simple message without any template.
     *                    In this case you must send isHtml = false. Otherwise you can send null
     * @param variables   of html template. If you want to send email with html template
     *                    and this template has some variables, you can send it here.
     *                    Note: You have to send isHtml = true. Otherwise you can send null
     * @param attachments if there is any otherwise null
     * @param template    if you want to send email with html template otherwise null
     * @param isHtml      if true then html email will be sent. In this case @param template cannot
     *                    be null.
     * @throws MessagingException if there is any exception
     */
    public void sendEmail(String to, String from, String subject, String body, Map<String, Object> variables, Map<String, InputStreamSource> attachments, Map<String, ClassPathResource> inlineResource, String template, boolean isHtml) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        if (attachments != null) {
            for (Map.Entry<String, InputStreamSource> entry : attachments.entrySet()) {
                String key = entry.getKey();
                InputStreamSource value = entry.getValue();
                helper.addAttachment(key, value);
            }
        }

        if (isHtml) {
            Context context = new Context();
            context.setVariables(variables);
            String html = templateEngine.process(template, context);
            helper.setText(html, true);
        } else {
            helper.setText(body);
        }

        // Always add inline resource after helper.setText().
        // Else, mail readers might not be able to resolve inline references correctly.
        if (inlineResource != null) {
            for (Map.Entry<String, ClassPathResource> entry : inlineResource.entrySet()) {
                String key = entry.getKey();
                ClassPathResource value = entry.getValue();
                helper.addInline(key, value);
            }
        }

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(from);

        mailSender.send(message);
    }
}
