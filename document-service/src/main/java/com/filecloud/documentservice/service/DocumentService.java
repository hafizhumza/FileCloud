package com.filecloud.documentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filecloud.documentservice.constant.ConstUtil;
import com.filecloud.documentservice.exception.RecordNotFoundException;
import com.filecloud.documentservice.model.db.Document;
import com.filecloud.documentservice.model.dto.*;
import com.filecloud.documentservice.network.EmailServiceClient;
import com.filecloud.documentservice.properties.DocumentServiceProperties;
import com.filecloud.documentservice.repository.DocumentRepository;
import com.filecloud.documentservice.security.dto.UserSession;
import com.filecloud.documentservice.security.util.AuthUtil;
import com.filecloud.documentservice.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

        String fileName = Util.getRandomUUID() + "." + ConstUtil.FILE_EXTENSION;
        Path targetLocation = fileStorageLocation.resolve(fileName);

        try {
            Files.copy(multipartDocument.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            error(e);
        }

        UserSession session = AuthUtil.getCurrentLoggedInUser();

        Document document = new Document();
        document.setUserId(session.getUserId());
        document.setDescription(properties.getDescription());
        document.setDisplayName(properties.getName());
        document.setDocumentType(properties.getType());
        document.setExtension(properties.getExtension());
        document.setPath(targetLocation.toString());
        document.setSizeInMb(documentSizeInMb);

        return new DocumentResponseDto(documentRepository.save(document));
    }

    public ByteArrayResource downloadDocument(long documentId) {
        Document document = getVerifiedUserDocument(documentId);
        Path path = Paths.get(document.getPath());

        try {
            return new ByteArrayResource(Files.readAllBytes(path));
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
        Document document = documentRepository.findById(documentId).orElseThrow(RecordNotFoundException::new);
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

    public void delete(DeleteRequestDto deleteRequestDto) {
        Document document = getVerifiedUserDocument(deleteRequestDto.getDocumentId());

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

        // TODO: Call Email Service to send email
        EmailSharedDocumentDto dto = new EmailSharedDocumentDto(
                requestDto.getReceiverEmail(),
                session.getFullName(),
                url,
                documentServiceProperties.getSharedDocumentsExpiryDays()
        );

        emailServiceClient.emailSharedDocumentUrl(AuthUtil.getBearerToken(), dto);
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
