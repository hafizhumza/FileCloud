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
import com.filecloud.documentservice.client.EmailServiceClient;
import com.filecloud.documentservice.constant.ConstUtil;
import com.filecloud.documentservice.exception.RecordNotFoundException;
import com.filecloud.documentservice.model.db.Document;
import com.filecloud.documentservice.model.dto.DocumentResponseDto;
import com.filecloud.documentservice.model.dto.DownloadDocumentDto;
import com.filecloud.documentservice.model.dto.EmailSharedDocumentDto;
import com.filecloud.documentservice.model.dto.ShareDocumentRequestDto;
import com.filecloud.documentservice.model.dto.SingleFieldResponse;
import com.filecloud.documentservice.model.dto.SingleIdRequestDto;
import com.filecloud.documentservice.model.dto.SpaceInfoResponseDto;
import com.filecloud.documentservice.model.dto.UpdateRequestDto;
import com.filecloud.documentservice.model.dto.UploadRequestDto;
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
		long documentSizeInBytes = verifyDocumentAndSpaceLimit(multipartDocument);

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
		document.setSizeInBytes(documentSizeInBytes);
		document.setRecycled(false);

		return new DocumentResponseDto(documentRepository.save(document));
	}

	public DownloadDocumentDto downloadDocument(long documentId) {
		Document document = getVerifiedUserDocument(documentId);

		try {
			return new DownloadDocumentDto(
					FileUtil.readFileToByteArray(document.getPath()),
					HeaderUtil.getDocumentHeaders(document));
		} catch (IOException e) {
			/*error("Document not found.");*/
		}

		return null;
	}

	public List<DocumentResponseDto> listActiveDocuments() {
		UserSession session = AuthUtil.getCurrentLoggedInUser();
		return documentRepository.findByUserIdAndRecycled(session.getUserId(), false)
				.stream()
				.map(DocumentResponseDto::new)
				.collect(Collectors.toList());
	}

	public DocumentResponseDto getDocument(long documentId) {
		Document document = documentRepository.findById(documentId).orElseThrow(() -> new RecordNotFoundException("Document not found"));
		return new DocumentResponseDto(document);
	}

	public SpaceInfoResponseDto getSpaceInfo() {
		long spaceLimit = Util.convertMbToBytes(documentServiceProperties.getSpaceLimitPerUser());
		long usedSpace = this.getUsedSpace();
		long remainingSpace = (spaceLimit - usedSpace);
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

		if (!document.getRecycled())
			invalidInput("Please add document to recycle bin first");

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

	public SingleFieldResponse countActiveDocuments() {
		UserSession user = AuthUtil.getCurrentLoggedInUser();
		return new SingleFieldResponse(documentRepository.countByUserIdAndRecycled(user.getUserId(), false));
	}

	private void createDocumentsDir(DocumentServiceProperties documentServiceProperties) {
		fileStorageLocation = Paths.get(documentServiceProperties.getUploadedDocumentsDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(fileStorageLocation);
		} catch (IOException e) {
			error(e);
		}
	}

	private long getUsedSpace() {
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

	private long verifyDocumentAndSpaceLimit(MultipartFile multipartDocument) {
		if (multipartDocument == null)
			invalidInput("Document cannot be null.");

		long documentSize = multipartDocument.getSize();

		if (documentSize <= 0)
			invalidInput("Document size cannot be 0.");

		SpaceInfoResponseDto spaceInfo = this.getSpaceInfo();

		if (documentSize > spaceInfo.getSpaceLimit())
			invalidInput(String.format("Your document size is bigger than space limit. " +
					"Document Size: %s, Space Limit: %s.", Util.humanReadableByteCountBin(documentSize), Util.humanReadableByteCountBin(documentSize)));

		if (documentSize > getSpaceInfo().getRemainingSpace())
			invalidInput(String.format("Your disk space is low. " +
					"Space Limit is %s. " +
					"You used %s. " +
					"Remaining space is %s. " +
					"Document size is %s. " +
					"Please delete some documents to continue.",
					Util.humanReadableByteCountBin(spaceInfo.getSpaceLimit()),
					Util.humanReadableByteCountBin(spaceInfo.getUsedSpace()),
					Util.humanReadableByteCountBin(spaceInfo.getRemainingSpace()),
					Util.humanReadableByteCountBin(documentSize)));

		return documentSize;
	}

	private Document getVerifiedUserDocument(long documentId) {
		Document document = documentRepository.findById(documentId).orElseThrow(RecordNotFoundException::new);
		UserSession session = AuthUtil.getCurrentLoggedInUser();

		if (!session.getUserId().equals(document.getUserId()))
			invalidAccess("This document doesn't belong to you.");

		return document;
	}

	public void recycle(SingleIdRequestDto dto) {
		Document document = getVerifiedUserDocument(dto.getId());
		document.setRecycled(true);
		documentRepository.save(document);
	}

	public void restore(SingleIdRequestDto dto) {
		Document document = getVerifiedUserDocument(dto.getId());
		document.setRecycled(false);
		documentRepository.save(document);
	}

	public List<DocumentResponseDto> listRecycledDocuments() {
		UserSession session = AuthUtil.getCurrentLoggedInUser();
		return documentRepository.findByUserIdAndRecycled(session.getUserId(), true)
				.stream()
				.map(DocumentResponseDto::new)
				.collect(Collectors.toList());
	}

	public SingleFieldResponse countRecycledDocuments() {
		UserSession user = AuthUtil.getCurrentLoggedInUser();
		return new SingleFieldResponse(documentRepository.countByUserIdAndRecycled(user.getUserId(), true));
	}

}
