package com.filecloud.documentservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filecloud.documentservice.constant.ConstUtil;
import com.filecloud.documentservice.exception.RecordNotFoundException;
import com.filecloud.documentservice.model.db.Document;
import com.filecloud.documentservice.model.dto.DocumentResponseDto;
import com.filecloud.documentservice.model.dto.DownloadDocumentDto;
import com.filecloud.documentservice.model.dto.EmailSharedDocumentDto;
import com.filecloud.documentservice.model.dto.ShareDocumentRequestDto;
import com.filecloud.documentservice.model.dto.SingleIdRequestDto;
import com.filecloud.documentservice.model.dto.SpaceInfoResponseDto;
import com.filecloud.documentservice.model.dto.UpdateRequestDto;
import com.filecloud.documentservice.model.dto.UploadRequestDto;
import com.filecloud.documentservice.network.EmailServiceClient;
import com.filecloud.documentservice.properties.DocumentServiceProperties;
import com.filecloud.documentservice.repository.DocumentRepository;
import com.filecloud.documentservice.response.Response.Status;
import com.filecloud.documentservice.response.Result;
import com.filecloud.documentservice.security.dto.UserSession;
import com.filecloud.documentservice.security.util.AuthUtil;
import com.filecloud.documentservice.util.FileUtil;
import com.filecloud.documentservice.util.HeaderUtil;
import com.filecloud.documentservice.util.Util;


@Service
public class DocumentService extends BaseService {

	private final DocumentServiceProperties documentServiceProperties;

	private final DocumentRepository documentRepository;

	private final SharedDocumentService sharedDocumentService;

	private final EmailServiceClient emailServiceClient;

	private static Path fileStorageLocation;

	private final ObjectMapper jacksonObjectMapper;

	private final Validator validator;

	@Autowired
	public DocumentService(DocumentRepository documentRepository, DocumentServiceProperties documentServiceProperties, ObjectMapper jacksonObjectMapper, Validator validator, SharedDocumentService sharedDocumentService, EmailServiceClient emailServiceClient) {
		this.documentRepository = documentRepository;
		this.sharedDocumentService = sharedDocumentService;
		this.documentServiceProperties = documentServiceProperties;
		this.emailServiceClient = emailServiceClient;
		this.jacksonObjectMapper = jacksonObjectMapper;
		this.validator = validator;
		createDocumentsDir(documentServiceProperties);
	}

	public DocumentResponseDto saveDocument(MultipartFile multipartDocument, String rawProps) {
		UploadRequestDto properties = verifyDocumentUploadRequest(rawProps);
		double documentSizeInMb = verifyDocumentAndSpaceLimit(multipartDocument);

		String fileName = Util.getRandomUUID() + "." + ConstUtil.FILE_EXTENSION_ENCRYPTED;
		Path targetLocation = fileStorageLocation.resolve(fileName);

		try {
			Files.copy(multipartDocument.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			error(e);
		}

		String extension = FilenameUtils.getExtension(multipartDocument.getOriginalFilename());
		UserSession session = AuthUtil.getCurrentLoggedInUser();

		Document document = new Document();
		document.setUserId(session.getUserId());
		document.setDescription(properties.getDescription());
		document.setDisplayName(properties.getName());
		document.setExtension(extension);
		document.setPath(targetLocation.toString());
		document.setSizeInMb(documentSizeInMb);

		return new DocumentResponseDto(documentRepository.save(document));
	}

	public DownloadDocumentDto downloadDocument(long documentId) {
		Document document = getVerifiedUserDocument(documentId);

		try {
			return new DownloadDocumentDto(
					FileUtil.readFileToByteArray(document.getPath()),
					HeaderUtil.getDocumentHeaders(document));
		} catch (IOException e) {
			error("Document not found.");
		}

		return null;
	}

	public List<DocumentResponseDto> listDocuments() {
		UserSession session = AuthUtil.getCurrentLoggedInUser();
		return documentRepository.findByUserId(session.getUserId())
				.stream().map(DocumentResponseDto::new).collect(Collectors.toList());
	}

	public DocumentResponseDto getDocument(long documentId) {
		Document document = documentRepository.findById(documentId).orElseThrow(() -> new RecordNotFoundException("Document not found"));
		return new DocumentResponseDto(document);
	}

	public SpaceInfoResponseDto getSpaceInfo() {
		double spaceLimit = documentServiceProperties.getSpaceLimitPerUser();
		double usedSpace = this.getUsedSpace();
		double remainingSpace = (spaceLimit - usedSpace);
		return new SpaceInfoResponseDto(spaceLimit, usedSpace, remainingSpace);
	}

	public DocumentResponseDto update(UpdateRequestDto updateRequestDto) {
		Document document = getVerifiedUserDocument(updateRequestDto.getDocumentId());
		document.setDisplayName(updateRequestDto.getName());
		document.setDescription(updateRequestDto.getDescription());
		return new DocumentResponseDto(documentRepository.save(document));
	}

	public void delete(SingleIdRequestDto deleteRequestDto) {
		Document document = getVerifiedUserDocument(deleteRequestDto.getId());

		try {
			Files.deleteIfExists(Paths.get(document.getPath()));
		} catch (IOException e) {
			error("Error while deleting file");
		}

		documentRepository.delete(document);
	}

	public void share(ShareDocumentRequestDto requestDto) {
		Document document = getVerifiedUserDocument(requestDto.getDocumentId());
		String url = sharedDocumentService.save(document);
		UserSession session = AuthUtil.getCurrentLoggedInUser();

		EmailSharedDocumentDto dto = new EmailSharedDocumentDto(
				requestDto.getReceiverEmail(),
				session.getFullName(),
				url,
				documentServiceProperties.getSharedDocumentsExpiryDays());

		Result<?> result = emailServiceClient.emailSharedDocumentUrl(AuthUtil.getBearerToken(), dto);

		if (!(result.isSuccess() && result.getStatusCode() == Status.ALL_OK.getStatusCode()))
			error(result.getStatusCode(), result.getMessage());
	}

	public void deleteUserDocuments(SingleIdRequestDto dto) {
		List<Document> documents = documentRepository.findByUserId(dto.getId());

		if (!Util.isValidList(documents))
			notFound("Documents not found");

		for (Document document : documents)
			try {
				Files.deleteIfExists(Paths.get(document.getPath()));
			} catch (IOException e) {
				error(e);
			}

		documentRepository.deleteAll(documents);
	}

	private void createDocumentsDir(DocumentServiceProperties documentServiceProperties) {
		fileStorageLocation = Paths.get(documentServiceProperties.getUploadedDocumentsDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(fileStorageLocation);
		} catch (IOException e) {
			error(e);
		}
	}

	private double getUsedSpace() {
		UserSession session = AuthUtil.getCurrentLoggedInUser();
		return documentRepository.findUsedSpace(session.getUserId());
	}

	private UploadRequestDto verifyDocumentUploadRequest(String rawProps) {
		UploadRequestDto properties = null;

		try {
			properties = jacksonObjectMapper.readValue(rawProps, UploadRequestDto.class);
		} catch (JsonProcessingException e) {
			error("Error while parsing");
		}

		Set<ConstraintViolation<UploadRequestDto>> violations = validator.validate(properties);

		if (!(violations == null || violations.isEmpty())) {
			String errors = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
			invalidInput(errors);
		}

		return properties;
	}

	private double verifyDocumentAndSpaceLimit(MultipartFile multipartDocument) {
		if (multipartDocument == null)
			invalidInput("Document cannot be null.");

		double documentSize = Util.convertBytesToMb(multipartDocument.getSize());

		if (documentSize <= 0)
			invalidInput("Document size cannot be 0.");

		SpaceInfoResponseDto spaceInfo = this.getSpaceInfo();

		if (documentSize > spaceInfo.getSpaceLimit())
			invalidAccess(String.format("Your document size is bigger than space limit. " +
					"Document Size: %.6f, Space Limit: %.6f.", documentSize, spaceInfo.getSpaceLimit()));

		if (documentSize > getSpaceInfo().getRemainingSpace())
			invalidAccess(String.format("Your disk space is low. " +
					"Space Limit is %.6f MB. " +
					"You used %.6f MB. " +
					"Remaining space is %.6f MB. " +
					"Document size is %.6f MB." +
					"Please delete some documents to continue.",
					spaceInfo.getSpaceLimit(), spaceInfo.getUsedSpace(),
					spaceInfo.getRemainingSpace(), documentSize));

		return documentSize;
	}

	private Document getVerifiedUserDocument(long documentId) {
		Document document = documentRepository.findById(documentId).orElseThrow(RecordNotFoundException::new);
		UserSession session = AuthUtil.getCurrentLoggedInUser();

		if (!session.getUserId().equals(document.getUserId()))
			invalidAccess("This document doesn't belong to you.");

		return document;
	}

}
