package com.filecloud.uiservice.util;

import com.filecloud.uiservice.properties.UiServiceProperties;
import com.filecloud.uiservice.security.encryption.CryptoUtils;
import feign.Response;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.crypto.CryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileUtil {

    private final UiServiceProperties uiServiceProperties;

    private static Path tempDir;

    @Autowired
    public FileUtil(UiServiceProperties uiServiceProperties) {
        this.uiServiceProperties = uiServiceProperties;
        createDocumentsDir(uiServiceProperties);
    }

    public MultipartFile getEncryptedMultipartFile(MultipartFile originalMultipartDocument) throws IOException, CryptoException {
        String originalFileName = Util.getRandomUUID() + ".encrypted";
        String encryptedFileName = Util.getRandomUUID() + ".encrypted";

        File originalFile = tempDir.resolve(originalFileName).toAbsolutePath().normalize().toFile();
        File encryptedFile = tempDir.resolve(encryptedFileName).toAbsolutePath().normalize().toFile();

        OutputStream outputStream = new FileOutputStream(originalFile);
        IOUtils.copy(originalMultipartDocument.getInputStream(), outputStream);
        outputStream.close();

        CryptoUtils.encrypt(uiServiceProperties.security().getEncryptionKey(), originalFile, encryptedFile);
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

    public ByteArrayResource decryptResponse(Response response) throws IOException, CryptoException {
        String originalFileName = Util.getRandomUUID() + ".encrypted";
        String decryptedFileName = Util.getRandomUUID() + ".encrypted";

        File originalFile = tempDir.resolve(originalFileName).toAbsolutePath().normalize().toFile();
        File decryptedFile = tempDir.resolve(decryptedFileName).toAbsolutePath().normalize().toFile();

        InputStream inputStream = response.body().asInputStream();
        Files.copy(inputStream, originalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        IOUtils.close(inputStream);

        CryptoUtils.decrypt(uiServiceProperties.security().getEncryptionKey(), originalFile, decryptedFile);
        Files.deleteIfExists(originalFile.toPath());

        byte[] content = Files.readAllBytes(decryptedFile.toPath());
        Files.deleteIfExists(decryptedFile.toPath());
        return new ByteArrayResource(content);
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
