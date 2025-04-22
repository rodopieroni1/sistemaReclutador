package com.sistemaReclutador.sistemaReclutador.controllers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/uploads")
public class FileUploadController {
    private final String UPLOAD_DIR = "C:/Users/Rodrigo/Documents/SistemaReclutadorFront/proyectoReclutador/src/assets/uploads"; // Cambia esto según tu estructura
    
    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.ok().body("El archivo está vacío");
        }
System.out.println("Pasa"+ file.getOriginalFilename());
        try {
            // Guardar archivo en UPLOAD_DIR
            File saveFile = new File(UPLOAD_DIR + file.getOriginalFilename());
            System.out.println("saveFile"+ saveFile);
            file.transferTo(saveFile);
            System.out.println("file.transferTo(saveFile)"+ file);

            return ResponseEntity.ok().body("{\"mensaje\": \"Archivo cargado exitosamente\"}");        
            } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }

}
