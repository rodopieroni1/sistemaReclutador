package com.sistemaReclutador.sistemaReclutador.dto;

import org.springframework.web.multipart.MultipartFile;
import com.sistemaReclutador.sistemaReclutador.Enum.ModalidadTrabajo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfertaUpdateRequest {

    @NotBlank(message = "El nombre de la oferta es obligatorio")
    private String nombreOferta;

    @NotBlank(message = "La descripción de la oferta es obligatoria")
    private String descripcionOferta;

    private boolean estadoOferta;

    @NotNull(message = "El ID de la empresa es obligatorio")
    private Long idEmpresa;

    private String fotoOferta;
    private MultipartFile fotoArchivo;

    // Ubicación específica del puesto de trabajo
    private String direccionTrabajo;
    private String localidadTrabajo;
    private String provinciaTrabajo;
    private String paisTrabajo;

    // PRESENCIAL, HIBRIDA o REMOTA
    private ModalidadTrabajo modalidad;
}
