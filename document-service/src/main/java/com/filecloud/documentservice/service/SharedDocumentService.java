package com.filecloud.documentservice.service;

import com.filecloud.documentservice.exception.RecordNotFoundException;
import com.filecloud.documentservice.model.db.Document;
import com.filecloud.documentservice.model.db.SharedDocument;
import com.filecloud.documentservice.model.dto.DownloadDocumentDto;
import com.filecloud.documentservice.properties.DocumentServiceProperties;
import com.filecloud.documentservice.repository.SharedDocumentRepository;
import com.filecloud.documentservice.util.HeaderUtil;
import com.filecloud.documentservice.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
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

    public DownloadDocumentDto getSharedDocument(String token) {
        SharedDocument sharedDocument = sharedDocumentRepository.findByToken(token)
                .orElseThrow(RecordNotFoundException::new);

        long expiryDaysMillis = Util.getDaysMillis(documentServiceProperties.getSharedDocumentsExpiryDays());
        long createDate = sharedDocument.getCreateDate();
        long expiryDate = expiryDaysMillis + createDate;
        long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis > expiryDate)
            invalidAccess("Link is expired");

        Document document = sharedDocument.getDocument();

        // TODO: Commented document decryption for now
//        String tempFile = Util.getRandomUUID() + "." + document.getExtension();
//        Path copyPath = Paths.get(documentServiceProperties.getUploadedDocumentsDir())
//                .resolve(tempFile)
//                .toAbsolutePath()
//                .normalize();


        try {
//            CryptoUtils.decrypt(documentServiceProperties.security().getEncryptionKey(), new File(document.getPath()), new File(copyPath.toString()));
//            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(copyPath));
//            Files.deleteIfExists(copyPath);
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(document.getPath())));
            return new DownloadDocumentDto(resource, HeaderUtil.getDocumentHeaders(document));
        } catch (IOException /*| CryptoException*/ e) {
            error("Document not found.");
        }

        return null;
    }

    // TODO: append gateway server path, either from .prop file or from code
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
