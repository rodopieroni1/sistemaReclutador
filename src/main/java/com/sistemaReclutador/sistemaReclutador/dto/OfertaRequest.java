package com.sistemaReclutador.sistemaReclutador.dto;

import org.springframework.web.multipart.MultipartFile;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import lombok.Data;

@Data
public class OfertaRequest {
    private String nombreOferta;
    private String descripcionOferta;
    private boolean estadoOferta;
    private String fotoOferta;
    private Empresa idEmpresa;
    private MultipartFile fotoArchivo;
    
    
}