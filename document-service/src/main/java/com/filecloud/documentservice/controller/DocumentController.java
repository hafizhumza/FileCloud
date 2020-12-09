package com.filecloud.documentservice.controller;

import com.filecloud.documentservice.model.dto.DeleteRequestDto;
import com.filecloud.documentservice.model.dto.DownloadDocumentDto;
import com.filecloud.documentservice.model.dto.ShareDocumentRequestDto;
import com.filecloud.documentservice.model.dto.UpdateRequestDto;
import com.filecloud.documentservice.response.Response;
import com.filecloud.documentservice.response.Result;
import com.filecloud.documentservice.security.role.User;
import com.filecloud.documentservice.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@User
@Transactional(readOnly = true)
@RequestMapping("api/v1/document")
@RestController
public class DocumentController extends BaseController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Transactional
    @PostMapping("/upload")
    public Result<?> upload(@RequestParam MultipartFile document, @RequestParam String properties) {
        return sendSuccessResponse(Response.Status.ALL_OK, "Document uploaded successfully!", documentService.saveDocument(document, properties));
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable long documentId) {
        DownloadDocumentDto dto = documentService.downloadDocument(documentId);

        return ResponseEntity.ok()
                .headers(dto.getHeaders())
                .contentLength(dto.getResource().contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dto.getResource());
    }

    @GetMapping("/list")
    public Result<?> listDocuments() {
        return sendSuccessResponse(Response.Status.ALL_OK, documentService.listDocuments());
    }

    @GetMapping("/{documentId}")
    public Result<?> getDocument(@PathVariable long documentId) {
        return sendSuccessResponse(Response.Status.ALL_OK, documentService.getDocument(documentId));
    }

    @Transactional
    @PostMapping("/update")
    public Result<?> update(@RequestBody @Valid UpdateRequestDto updateRequestDto) {
        return sendSuccessResponse(Response.Status.ALL_OK, "Document updated successfully!", documentService.update(updateRequestDto));
    }

    @Transactional
    @PostMapping("/delete")
    public Result<?> delete(@RequestBody @Valid DeleteRequestDto deleteRequestDto) {
        documentService.delete(deleteRequestDto);
        return sendSuccessResponse(Response.Status.ALL_OK, "Document deleted successfully!");
    }

    @GetMapping("/used-space")
    public Result<?> usedSpace() {
        return sendSuccessResponse(Response.Status.ALL_OK, documentService.getSpaceInfo());
    }

    @Transactional
    @PostMapping("/share")
    public Result<?> share(@RequestBody @Valid ShareDocumentRequestDto requestDto) {
        documentService.share(requestDto);
        return sendSuccessResponse(Response.Status.ALL_OK, "Document shared successfully to the given email address");
    }

}
