package com.filecloud.uiservice.controller;

import com.filecloud.uiservice.dto.mvcmodel.DownloadDocumentModel;
import com.filecloud.uiservice.exception.RecordNotFoundException;
import com.filecloud.uiservice.service.SharedDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("shared/documents")
@Controller
public class SharedDocumentController extends BaseController {

    private final SharedDocumentService sharedDocumentService;

    @Autowired
    public SharedDocumentController(SharedDocumentService sharedDocumentService) {
        this.sharedDocumentService = sharedDocumentService;
    }

    @GetMapping("/download/{token}")
    public ResponseEntity<ByteArrayResource> downloadSharedDocument(@PathVariable String token, Model model) {
        DownloadDocumentModel dto = sharedDocumentService.downloadSharedDocument(token);

        if (dto == null || dto.getResource() == null)
            throw new RecordNotFoundException();

        return ResponseEntity.ok()
                .headers(dto.getHeaders())
                .contentLength(dto.getResource().contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dto.getResource());
    }

}
