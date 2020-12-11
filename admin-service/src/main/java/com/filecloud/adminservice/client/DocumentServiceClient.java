package com.filecloud.adminservice.client;

import com.filecloud.adminservice.model.dto.SingleIdRequestDto;
import com.filecloud.adminservice.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "DocumentService", path = "api/v1/document-management")
public interface DocumentServiceClient {

    @PostMapping("/delete-user-documents")
    Result<?> deleteUserDocuments(@RequestHeader("Authorization") String bearerToken, SingleIdRequestDto dto);

}
