package com.sistemaReclutador.sistemaReclutador.dto;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OfertaRequest {
    private String nombreOferta;
    private String descripcionOferta;
    private Boolean estadoOferta; 
    @NotNull(message = "El ID de la empresa es obligatorio")
    private Long idEmpresa; 
    private Long idUsuario;
    private String fotoOferta;
    private MultipartFile fotoArchivo;
    public boolean isEstadoOferta() {
        return Boolean.TRUE.equals(this.estadoOferta);
    }
}