package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.controllers.MyWebSocketHandler;
import com.sistemaReclutador.sistemaReclutador.services.FileUploadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private final MyWebSocketHandler webSocketHandler;

    @Override
    public String upload(MultipartFile file, String tipo) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío.");
        }

        String nombreOriginal = file.getOriginalFilename();

        if (nombreOriginal == null || nombreOriginal.isBlank()) {
            throw new IllegalArgumentException("El archivo no tiene nombre.");
        }

        String subCarpeta = determinarCarpeta(nombreOriginal, tipo);

        String nombreLimpio = nombreOriginal
                .replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

        String nombreUnico =
                UUID.randomUUID() + "_" + nombreLimpio;

        try {

            Path directorioDestino =
                    Paths.get(uploadDir, subCarpeta);

            Files.createDirectories(directorioDestino);

            Path archivoDestino =
                    directorioDestino.resolve(nombreUnico);

            file.transferTo(archivoDestino);

            log.info(
                "Archivo guardado correctamente en: {}",
                archivoDestino.toAbsolutePath()
            );

            webSocketHandler.notifyClients(
                archivoDestino.toAbsolutePath().toString()
            );

            return nombreUnico;

        } catch (IOException e) {

            throw new RuntimeException(
                "Error al guardar el archivo",
                e
            );
        }
    }

    private String determinarCarpeta(
            String nombreOriginal,
            String tipo) {

        if ("oferta".equalsIgnoreCase(tipo)) {
            return "ofertas";
        }

        String nombre = nombreOriginal.toLowerCase();

        if (nombre.endsWith(".png")
                || nombre.endsWith(".jpg")
                || nombre.endsWith(".jpeg")) {

            return "fotos";
        }

        return "documentos";
    }
}