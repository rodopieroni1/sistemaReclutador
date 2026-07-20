package com.sistemaReclutador.sistemaReclutador.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.upload.dir}")
    private String uploadDir;
    
    @Autowired
    private MyWebSocketHandler webSocketHandler;
    
    @PostMapping
    public ResponseEntity<Map<String,String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "tipo", required = false) String tipo) throws IOException {
            
        if (file.isEmpty()) {
             Map<String, String> respuesta = new HashMap<>();
             respuesta.put("mensaje", "Archivo Inexistente");
             return ResponseEntity.ok().body(respuesta);
        }
        try {
            File directorioBase = new File(uploadDir);
            String nombreOriginal = file.getOriginalFilename();
            
            // 1. Clasificación del tipo de carpeta
            String subCarpeta = "documentos"; 
            if (tipo != null && tipo.equalsIgnoreCase("oferta")) {
                subCarpeta = "ofertas"; 
            } else if (nombreOriginal != null && (nombreOriginal.toLowerCase().endsWith(".png") || 
                                                  nombreOriginal.toLowerCase().endsWith(".jpg") || 
                                                  nombreOriginal.toLowerCase().endsWith(".jpeg"))) {
                subCarpeta = "fotos";
            }
            
            // 2. Generar un nombre único para evitar repeticiones
            // Limpiamos el nombre original de caracteres raros primero
            String nombreLimpio = nombreOriginal.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
            
            // Le agregamos un UUID único por delante: "UUID_nombre_limpio.jpg"
            String nombreUnico = java.util.UUID.randomUUID().toString() + "_" + nombreLimpio;
            
            // 3. Apuntar al directorio destino
            File directorioDestino = new File(directorioBase, subCarpeta);
            if (!directorioDestino.exists()) {
                directorioDestino.mkdirs(); 
            }

            // 4. Guardar físicamente el archivo con el nuevo NOMBRE ÚNICO
            File saveFile = new File(directorioDestino, nombreUnico);
            file.transferTo(saveFile);
            
            System.out.println("=========================================");
            System.out.println("¡GUARDADO REAL EN!: " + saveFile.getAbsolutePath());
            System.out.println("=========================================");

            webSocketHandler.notifyClients(saveFile.getAbsolutePath());
            
            // 5. Devolver la URL con el nuevo nombre único para que impacte en la BD
            Map<String, String> respuesta = new HashMap<>();
            String urlAcceso = "/uploads/" + subCarpeta + "/" + nombreUnico;
            respuesta.put("url", urlAcceso);

            return ResponseEntity.ok().body(respuesta);
        
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }
}