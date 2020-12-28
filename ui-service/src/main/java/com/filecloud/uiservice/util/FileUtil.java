package com.filecloud.uiservice.util;

import com.filecloud.uiservice.dto.mvcmodel.DocumentUploadModel;
import com.filecloud.uiservice.properties.UiServiceProperties;
import com.filecloud.uiservice.security.encryption.CryptoUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.crypto.CryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileUtil {

    private final UiServiceProperties uiServiceProperties;

    private static Path tempDir;

    @Autowired
    public FileUtil(UiServiceProperties uiServiceProperties) {
        this.uiServiceProperties = uiServiceProperties;
        createDocumentsDir(uiServiceProperties);
    }

    public ByteArrayResource readFileToByteArray(String pathString) throws IOException {
        Path path = Paths.get(pathString);
        return new ByteArrayResource(Files.readAllBytes(path));
    }

    public MultipartFile getEncryptedMultipartFile(MultipartFile originalMultipartDocument) throws IOException, CryptoException {
        String extension = FilenameUtils.getExtension(originalMultipartDocument.getOriginalFilename());
        String originalFileName = Util.getRandomUUID() + "." + extension;
        String encryptedFileName = Util.getRandomUUID() + "." + extension;

        File originalFile = tempDir.resolve(originalFileName).toAbsolutePath().normalize().toFile();
        File encryptedFile = tempDir.resolve(encryptedFileName).toAbsolutePath().normalize().toFile();

        OutputStream outputStream = new FileOutputStream(originalFile);
        IOUtils.copy(originalMultipartDocument.getInputStream(), outputStream);

        CryptoUtils.encrypt(uiServiceProperties.security().getEncryptionKey(), originalFile, encryptedFile);

        outputStream.close();
        Files.deleteIfExists(originalFile.toPath());

        byte[] content = Files.readAllBytes(encryptedFile.toPath());
        Files.deleteIfExists(encryptedFile.toPath());

        return new MockMultipartFile(
                originalMultipartDocument.getName(),
                originalMultipartDocument.getOriginalFilename(),
                originalMultipartDocument.getContentType(),
                content
        );
    }

    private void createDocumentsDir(UiServiceProperties uiServiceProperties) {
        tempDir = Paths.get(uiServiceProperties.getTempDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(tempDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
