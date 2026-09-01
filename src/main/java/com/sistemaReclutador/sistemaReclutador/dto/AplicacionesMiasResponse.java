package com.sistemaReclutador.sistemaReclutador.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AplicacionesMiasResponse {

    private Integer idAplicacion;
    private String puesto;
    private String empresa;
    private LocalDateTime fecha;
    private Boolean estado;
    private String descripcionOferta;
    private String fotoOferta;
    private String email;
    private String telefono;
    private String direccion;
    private Long idOferta;
    
}