package com.sistemaReclutador.sistemaReclutador.strategies.impl;

import java.nio.file.*;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.sistemaReclutador.sistemaReclutador.strategies.FileStorageStrategy;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageStrategyImpl implements FileStorageStrategy {
	
	@Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.base.url}")
    private String appBaseUrl;

    @Override
    public String storeFile(MultipartFile file, String subFolder) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String fileName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
            Path targetDir = Paths.get(uploadDir, subFolder).normalize();
            Files.createDirectories(targetDir);
            Files.copy(file.getInputStream(), targetDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return appBaseUrl + "/uploads/" + subFolder + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo en " + subFolder, e);
        }
    }
}
