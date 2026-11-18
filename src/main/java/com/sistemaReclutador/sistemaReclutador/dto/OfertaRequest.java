package com.sistemaReclutador.sistemaReclutador.dto;

import org.springframework.web.multipart.MultipartFile;
import com.sistemaReclutador.sistemaReclutador.Enum.ModalidadTrabajo;
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

    // Ubicación específica del puesto de trabajo
    private String direccionTrabajo;
    private String localidadTrabajo;
    private String provinciaTrabajo;
    private String paisTrabajo;

    // PRESENCIAL, HIBRIDA o REMOTA
    private ModalidadTrabajo modalidad;

    public boolean isEstadoOferta() {
        return Boolean.TRUE.equals(this.estadoOferta);
    }
}
