package com.sistemaReclutador.sistemaReclutador.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.entities.Archivos;
import com.sistemaReclutador.sistemaReclutador.repositories.ArchivosRepository;

@Service
public class ArchivosServices {
    @Autowired
    private ArchivosRepository archivoRepository;

    public Archivos guardarArchivo(MultipartFile archivos) throws IOException {
        Archivos nuevoArchivo = new Archivos();
        nuevoArchivo.setNombre(archivos.getOriginalFilename());
        nuevoArchivo.setContenido(archivos.getBytes());
        return archivoRepository.save(nuevoArchivo);
    }

    public Archivos obtenerArchivo(Long id) {
        return archivoRepository.findById(id).orElse(null);
    }
}
