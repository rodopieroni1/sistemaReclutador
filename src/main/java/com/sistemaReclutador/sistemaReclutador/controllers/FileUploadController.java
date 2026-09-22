package com.sistemaReclutador.sistemaReclutador.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.services.FileUploadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "tipo", required = false) String tipo) {

        String url = fileUploadService.upload(file, tipo);

        return ResponseEntity.ok(
                Map.of("url", url)
        );
    }
}