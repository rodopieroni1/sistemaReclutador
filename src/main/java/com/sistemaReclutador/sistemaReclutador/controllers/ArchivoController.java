package com.sistemaReclutador.sistemaReclutador.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.entities.Archivos;
import com.sistemaReclutador.sistemaReclutador.services.ArchivosServices;

@RestController
@RequestMapping("/archivos")
public class ArchivoController {
    @Autowired
    private ArchivosServices archivosServices;

    @PostMapping("/subir")
    public ResponseEntity<Archivos> subirArchivo(@RequestParam("archivo") MultipartFile archivo) {
        try {
            Archivos archivoGuardado = archivosServices.guardarArchivo(archivo);
            return ResponseEntity.ok(archivoGuardado);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/descargar/{id}")
    public ResponseEntity<byte[]> descargarArchivo(@PathVariable Long id) {
        Archivos archivo = archivosServices.obtenerArchivo(id);
        if (archivo == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getNombre() + "\"")
                .body(archivo.getContenido());
    }
}
