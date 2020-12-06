package com.filecloud.documentservice.controller;

import com.filecloud.documentservice.service.SharedDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

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
        ByteArrayResource resource = sharedDocumentService.getSharedDocument(token);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AnyDesk.exe");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
