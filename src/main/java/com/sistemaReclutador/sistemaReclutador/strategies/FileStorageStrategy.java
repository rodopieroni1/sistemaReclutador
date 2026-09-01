package com.sistemaReclutador.sistemaReclutador.strategies;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageStrategy {
	String storeFile(MultipartFile file, String subFolder);
}
