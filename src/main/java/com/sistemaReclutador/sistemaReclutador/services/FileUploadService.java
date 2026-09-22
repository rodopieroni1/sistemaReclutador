package com.sistemaReclutador.sistemaReclutador.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    String upload(MultipartFile file, String tipo);

}
