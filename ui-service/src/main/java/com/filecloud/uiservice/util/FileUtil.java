package com.filecloud.uiservice.util;

import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    public static ByteArrayResource readFileToByteArray(String pathString) throws IOException {
        Path path = Paths.get(pathString);
        return new ByteArrayResource(Files.readAllBytes(path));
    }
}
