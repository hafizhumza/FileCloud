package com.filecloud.documentservice.controller;

import com.filecloud.documentservice.model.dto.DownloadDocumentDto;
import com.filecloud.documentservice.service.SharedDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/document/shared")
@RestController
public class SharedDocumentController extends BaseController {

    private final SharedDocumentService sharedDocumentService;

    @Autowired
    public SharedDocumentController(SharedDocumentService sharedDocumentService) {
        this.sharedDocumentService = sharedDocumentService;
    }

    @GetMapping("/{token}")
    public ResponseEntity<ByteArrayResource> document(@PathVariable String token) {
        DownloadDocumentDto dto = sharedDocumentService.getSharedDocument(token);

        return ResponseEntity.ok()
                .headers(dto.getHeaders())
                .contentLength(dto.getResource().contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dto.getResource());
    }
}
