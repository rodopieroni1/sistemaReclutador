package com.sistemaReclutador.sistemaReclutador.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AplicacionRequest {

    @JsonProperty("fechaAplicacion")
    private LocalDateTime fechaAplicacion;

    @JsonProperty("estadoAplicaciones")
    private boolean estadoAplicaciones;

    @JsonProperty("idPerfil")
    private Perfil idPerfil;

    @JsonProperty("idOferta")
    private Oferta idOferta;
}