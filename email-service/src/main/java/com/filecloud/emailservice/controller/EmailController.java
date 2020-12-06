package com.filecloud.emailservice.controller;

import com.filecloud.emailservice.model.dto.EmailSharedDocumentUrlDto;
import com.filecloud.emailservice.response.Response;
import com.filecloud.emailservice.response.Result;
import com.filecloud.emailservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result emailSharedDocumentUrl(@RequestBody EmailSharedDocumentUrlDto dto) {
        emailService.saveAndSend(dto);
        return sendSuccessResponse(Response.Status.ALL_OK, "Email has been sent successfully");
    }
}
