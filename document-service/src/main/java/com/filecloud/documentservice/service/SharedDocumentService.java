package com.filecloud.documentservice.service;

import com.filecloud.documentservice.exception.RecordNotFoundException;
import com.filecloud.documentservice.model.db.Document;
import com.filecloud.documentservice.model.db.SharedDocument;
import com.filecloud.documentservice.properties.DocumentServiceProperties;
import com.filecloud.documentservice.repository.SharedDocumentRepository;
import com.filecloud.documentservice.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SharedDocumentService extends BaseService {

    private final SharedDocumentRepository sharedDocumentRepository;

    private final DocumentServiceProperties documentServiceProperties;

    @Autowired
    public SharedDocumentService(SharedDocumentRepository sharedDocumentRepository, DocumentServiceProperties documentServiceProperties) {
        this.sharedDocumentRepository = sharedDocumentRepository;
        this.documentServiceProperties = documentServiceProperties;
    }

    public String save(Document document) {
        String token = Util.getRandomUUID();

        SharedDocument sharedDocument = new SharedDocument();
        sharedDocument.setToken(token);
        sharedDocument.setDocument(document);
        sharedDocumentRepository.save(sharedDocument);

        return getSharedDocumentUrl(token);
    }

    public ByteArrayResource getSharedDocument(String token) {
        SharedDocument sharedDocument = sharedDocumentRepository.findByToken(token)
                .orElseThrow(RecordNotFoundException::new);

        long expiryDaysMillis = Util.getDaysMillis(documentServiceProperties.getSharedDocumentsExpiryDays());
        long createDate = sharedDocument.getCreateDate();
        long expiryDate = expiryDaysMillis + createDate;
        long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis > expiryDate)
            invalidAccess("Link is expired");

        Document document = sharedDocument.getDocument();
        Path path = Paths.get(document.getPath());

        try {
            return new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException e) {
            error("Document not found.");
        }

        return null;
    }

    //    public Resource loadFileAsResource(String fileName) throws Exception {
//        try {
//            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//            if (resource.exists()) {
//                return resource;
//            } else {
//                throw new FileNotFoundException("File not found " + fileName);
//            }
//        } catch (MalformedURLException ex) {
//            throw new FileNotFoundException("File not found " + fileName);
//        }
//    }
//
//    public String getDocumentName(Integer userId, String docType) {
//        return docStorageRepo.getUploadDocumnetPath(userId, docType);
//    }

    private String getSharedDocumentUrl(String token) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api")
                .path("/v1")
                .path("/document")
                .path("/shared/")
                .path(token)
                .toUriString();
    }

}
