package com.sistemaReclutador.sistemaReclutador.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/uploads/")
public class FileUploadController {
    private final String UPLOAD_DIR = "C:/Users/Rodrigo/Documents/SistemaReclutadorFront/proyectoReclutador/src/assets/uploads/fotos/"; // Cambia esto según tu estructura
    
    @Autowired
    private MyWebSocketHandler webSocketHandler;
    
    @PostMapping
    public ResponseEntity<Map<String,String>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
        	 Map<String, String> respuesta = new HashMap<>();
             respuesta.put("mensaje", "Archivo Inexistente");
             return ResponseEntity.ok().body(respuesta);
        }
        try {
            // Guardar archivo en UPLOAD_DIR
            File saveFile = new File(UPLOAD_DIR + file.getOriginalFilename());
            file.transferTo(saveFile);
            String saveFile1 = saveFile.toString();
            System.out.println("Imagen subida: " + saveFile1);

            // Notificar a los clientes conectados
            webSocketHandler.notifyClients(saveFile1);
            
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Archivo con foto cargado exitosamente");
            return ResponseEntity.ok().body(respuesta);
        
            } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }

}
