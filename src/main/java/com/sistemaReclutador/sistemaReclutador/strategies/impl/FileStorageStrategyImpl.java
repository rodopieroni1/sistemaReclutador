package com.sistemaReclutador.sistemaReclutador.strategies.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.strategies.FileStorageStrategy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageStrategyImpl implements FileStorageStrategy {

	private static final List<String> EXTENSIONES_PERMITIDAS = List.of("image/png", "image/jpeg", "image/webp");

	private static final Set<String> EXTENSIONES_DOCUMENTO_PERMITIDAS = Set.of("application/pdf",
			// Microsoft Word
			"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
			// Microsoft Excel
			"application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
			// Microsoft PowerPoint
			"application/vnd.ms-powerpoint",
			"application/vnd.openxmlformats-officedocument.presentationml.presentation",
			// OpenDocument
			"application/vnd.oasis.opendocument.text", "application/vnd.oasis.opendocument.spreadsheet",
			"application/vnd.oasis.opendocument.presentation");

	@Value("${app.upload.dir}")
	private String uploadDir;

	@Value("${app.base.url}")
	private String appBaseUrl;

	@Override
	public String storeFile(MultipartFile file, String subFolder) {
		if (file == null || file.isEmpty()) {
			return null;
		}
		String contentType = file.getContentType();
		if ("fotos".equals(subFolder)) {
			if (contentType == null || !EXTENSIONES_PERMITIDAS.contains(contentType)) {
				throw new IllegalArgumentException("Formato de imagen no permitido. Solo se aceptan PNG, JPEG y WEBP.");
			}
		} else if ("documentos".equals(subFolder)) {
			if (contentType == null || !EXTENSIONES_DOCUMENTO_PERMITIDAS.contains(contentType)) {
				throw new IllegalArgumentException("Formato de CV no permitido. Solo se aceptan archivos PDF.");
			}
		}
		try {
			String fileName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
			Path targetDir = Paths.get(uploadDir, subFolder).normalize();
			Files.createDirectories(targetDir);
			Files.copy(file.getInputStream(), targetDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
			return "/uploads/" + subFolder + "/" + fileName;
		} catch (IOException e) {

			throw new RuntimeException("Error al guardar el archivo en " + subFolder, e);
		}
	}
}