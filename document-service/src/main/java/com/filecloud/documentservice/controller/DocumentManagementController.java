package com.filecloud.documentservice.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filecloud.documentservice.model.dto.SingleIdRequestDto;
import com.filecloud.documentservice.response.Response;
import com.filecloud.documentservice.response.Result;
import com.filecloud.documentservice.security.role.Admin;
import com.filecloud.documentservice.service.DocumentService;


@Admin
@Transactional(readOnly = true)
@RequestMapping("api/v1/document-management")
@RestController
public class DocumentManagementController extends BaseController {

	private final DocumentService documentService;

	@Autowired
	public DocumentManagementController(DocumentService documentService) {
		this.documentService = documentService;
	}

	@Transactional
	@PostMapping("/delete-user-documents")
	public Result<?> deleteUserDocuments(@RequestBody @Valid SingleIdRequestDto dto) {
		documentService.deleteUserDocuments(dto);
		return sendSuccessResponse(Response.Status.ALL_OK, "Documents deleted successfully!");
	}

}
