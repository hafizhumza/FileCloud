package com.filecloud.documentservice.client;

import com.filecloud.documentservice.model.dto.EmailSharedDocumentDto;
import com.filecloud.documentservice.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "EmailService", path = "api/v1/email")
public interface EmailServiceClient {

    @PostMapping("/email-shared-document")
    Result<?> emailSharedDocumentUrl(
            @RequestHeader("Authorization") String bearerToken,
            EmailSharedDocumentDto dto
    );

}
