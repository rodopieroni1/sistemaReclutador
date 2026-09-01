package com.sistemaReclutador.sistemaReclutador.dto;

import com.sistemaReclutador.sistemaReclutador.Enum.ResultadosAplicacion;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AplicacionResponseDTO {

    private ResultadosAplicacion status;
    private String mensaje;
}